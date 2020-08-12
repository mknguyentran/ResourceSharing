/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.RequestDTO;
import dto.ResourceDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import utilities.DBConnection;
import utilities.Generate;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class ResourceDAO extends DAO {

    public static final String TABLE_NAME = "Resource";
    public static final Logger logger = Logger.getLogger(ResourceDAO.class);
    public static final int PAGE_STEP = 5;

    public List<ResourceDTO> searchResource(String searchName, Timestamp searchFromDate, Timestamp searchToDate, int searchCategory, String role, int page) throws Exception {
        List<ResourceDTO> resourceList = null;
        ResourceDTO resource = null;
        int id, quantity, availableAmount, index = 1;
        String name, category, color, searchString = "", availableAmountSQL = "";
        availableAmountSQL = Generate.generateAvailableAmountSQL(searchFromDate, searchToDate);
        searchString = Generate.generateResourceSearchString(searchName, searchCategory, searchFromDate, searchToDate, role);
        String sql = "Select ID, Name, dbo.RESOURCECATEGORYNAME(Category) AS Category, Color, Quantity, " + availableAmountSQL + " From " + TABLE_NAME + searchString + " ORDER BY Name ASC OFFSET ? ROWS FETCH NEXT " + PAGE_STEP + " ROWS ONLY";
        Logging.logDebug(logger, "sql", sql);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (searchFromDate != null && searchToDate != null) {
                ps.setTimestamp(index++, searchFromDate);
                ps.setTimestamp(index++, searchToDate);
            } else if (searchFromDate != null) {
                ps.setTimestamp(index++, searchFromDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
                ps.setTimestamp(index++, searchFromDate);
            } else if (searchToDate != null) {
                ps.setTimestamp(index++, searchToDate);
                ps.setTimestamp(index++, searchToDate);
            }
            if (searchName != null) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (searchCategory > 0) {
                ps.setInt(index++, searchCategory);
            }
            ps.setInt(index, (page - 1) * PAGE_STEP);
            rs = ps.executeQuery();
            resourceList = new ArrayList<>();
            while (rs.next()) {
                id = rs.getInt("ID");
                name = rs.getString("Name");
                category = rs.getString("Category");
                quantity = rs.getInt("Quantity");
                color = rs.getString("Color");
                availableAmount = rs.getInt("AvailableAmount");
                resource = new ResourceDTO(id, quantity, availableAmount, name, category, color);
                resourceList.add(resource);
            }
        } finally {
            closeConnection();
        }
        return resourceList;
    }

    public int getPagesAmount(String searchName, Timestamp searchFromDate, Timestamp searchToDate, int searchCategory, String role) throws Exception {
        int index = 1;
        double pagesAmount = 0;
        String searchString = "";
        searchString = Generate.generateResourceSearchString(searchName, searchCategory, searchFromDate, searchToDate, role);
        String sql = "Select Count(ID) AS Count From " + TABLE_NAME + searchString;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
//            if (searchFromDate != null && searchToDate != null) {
//                ps.setTimestamp(index++, searchFromDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//                ps.setTimestamp(index++, searchToDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//            } else if (searchFromDate != null) {
//                ps.setTimestamp(index++, searchFromDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//                ps.setTimestamp(index++, searchFromDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//            } else if (searchToDate != null) {
//                ps.setTimestamp(index++, searchToDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//                ps.setTimestamp(index++, searchToDate, Calendar.getInstance(TimeZone.getTimeZone("UTC")));
//            }
            if (searchName != null) {
                ps.setString(index++, "%" + searchName + "%");
            }
            if (searchCategory > 0) {
                ps.setInt(index++, searchCategory);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                pagesAmount = rs.getInt("Count");
                pagesAmount = Math.ceil(pagesAmount / PAGE_STEP);
            }
        } finally {
            closeConnection();
        }
        return (int) pagesAmount;
    }

    public HashMap<ResourceDTO, Integer> loadDisplayCart(RequestDTO request, String role) throws Exception {
        HashMap<ResourceDTO, Integer> result = null;
        ResourceDTO resource = null;
        String roleCondition = "";
        if (role.equals("leader")) {
            roleCondition = " AND (RequestAuthority = dbo.ACCOUNTROLEID('leader') OR RequestAuthority = dbo.ACCOUNTROLEID('employee'))";
        } else if (role.equals("employee")) {
            roleCondition = " AND RequestAuthority = dbo.ACCOUNTROLEID('employee')";
        }
        String name, color, category;
        int availableAmount;
        boolean isSufficient;
        String sql = "Select Name, dbo.RESOURCECATEGORYNAME(Category) AS Category, Color, dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, ?, ?) AS AvailableAmount From " + TABLE_NAME + " Where ID = ? " + roleCondition;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, request.getFromDate());
            ps.setTimestamp(2, request.getToDate());
            result = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : request.getCart().entrySet()) {
                ps.setInt(3, entry.getKey());
                rs = ps.executeQuery();
                if (rs.next()) {
                    name = rs.getString("Name");
                    color = rs.getString("Color");
                    category = rs.getString("Category");
                    availableAmount = rs.getInt("AvailableAmount");
                    isSufficient = availableAmount > 0;
                    resource = new ResourceDTO(entry.getKey(), name, category, color);
                    resource.setSufficient(isSufficient);
                    if (entry.getValue() <= availableAmount) {
                        result.put(resource, entry.getValue());
                    } else {
                        result.put(resource, availableAmount);
                    }
                } else {
                    request.remove(entry.getKey());
                }
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public int getAvailableAmount(int id, Timestamp fromDate, Timestamp toDate) throws Exception {
        int result = 0;
        String sql = "Select dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, ?, ?) AS AvailableAmount From " + TABLE_NAME + " Where ID = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, fromDate);
            ps.setTimestamp(2, toDate);
            ps.setInt(3, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("AvailableAmount");
            }
        } finally {
            closeConnection();
        }
        return result;
    }
}
