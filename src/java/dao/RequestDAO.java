/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.RequestDTO;
import dto.ResourceDTO;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import utilities.DBConnection;
import utilities.Generate;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class RequestDAO extends DAO {

    public static final Logger logger = Logger.getLogger(RequestDAO.class);
    public static final String REQUEST_TABLE_NAME = "Request";
    public static final String REQUEST_DETAIL_TABLE_NAME = "RequestDetail";
    public static final int PAGE_STEP = 5;

    public boolean isSufficient(RequestDTO request, String role) throws Exception {
        boolean sufficient = false;
        String roleCondition = "";
        if (role.equals("leader")) {
            roleCondition = " AND (RequestAuthority = dbo.ACCOUNTROLEID('leader') OR RequestAuthority = dbo.ACCOUNTROLEID('employee'))";
        } else if (role.equals("employee")) {
            roleCondition = " AND RequestAuthority = dbo.ACCOUNTROLEID('employee')";
        }
        String sql = "Select dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, ?, ?) AS AvailableAmount From " + ResourceDAO.TABLE_NAME + " Where ID = ? " + roleCondition;
        Logging.logDebug(logger, "sqlIsSufficient", sql);
        Logging.logDebug(logger, "role", role);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, request.getFromDate());
            ps.setTimestamp(2, request.getToDate());
            for (Map.Entry<Integer, Integer> entry : request.getCart().entrySet()) {
                ps.setInt(3, entry.getKey());
                rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("AvailableAmount") < entry.getValue()) {
                        Logging.logDebug(logger, "failed 1", entry.getKey());
                        return sufficient;
                    }
                } else {
                    Logging.logDebug(logger, "failed 2", entry.getKey());
                    return sufficient;
                }
            }
            sufficient = true;
        } finally {
            closeConnection();
        }
        return sufficient;
    }

    public boolean isSufficient(int id) throws Exception {
        boolean sufficient = false;
        Timestamp fromDate, toDate;
        int resourceAmount, resourceAvailabilityAmount;
        String sql1 = "Select FromDate, ToDate From " + REQUEST_TABLE_NAME + " Where Status != 4 AND ID = ?";
        String sql2 = "Select Amount, dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(r.ID, ?, ?) AS AvailableAmount From " + REQUEST_DETAIL_TABLE_NAME + " rd INNER JOIN " + ResourceDAO.TABLE_NAME + " r ON rd.ResourceID = r.ID Where rd.RequestID = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                fromDate = rs.getTimestamp("FromDate");
                toDate = rs.getTimestamp("ToDate");
                ps = conn.prepareStatement(sql2);
                ps.setTimestamp(1, fromDate);
                ps.setTimestamp(2, toDate);
                ps.setInt(3, id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    resourceAvailabilityAmount = rs.getInt("AvailableAmount");
                    resourceAmount = rs.getInt("Amount");
                    if (resourceAmount > resourceAvailabilityAmount) {
                        return sufficient;
                    }
                }
                sufficient = true;
            }
        } finally {
            closeConnection();
        }
        return sufficient;
    }

    public int createRequest(RequestDTO request, String email) throws Exception {
        int requestID = -1;
        String sql = "Insert Into " + REQUEST_TABLE_NAME + "(SentAt, FromDate, ToDate, RequestedUser) values(CURRENT_TIMESTAMP,?,?,?)";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, request.getFromDate());
            ps.setTimestamp(2, request.getToDate());
            ps.setString(3, email);
            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    requestID = rs.getInt(1);
                }
            }
        } finally {
            closeConnection();
        }
        return requestID;
    }

    public boolean addRequestDetail(int requestID, HashMap<Integer, Integer> cart) throws Exception {
        boolean successful = false;
        int[] result = null;
        String sql = "Insert into " + REQUEST_DETAIL_TABLE_NAME + "(RequestID, ResourceID, Amount) values (?,?,?)";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, requestID);
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                ps.setInt(2, entry.getKey());
                ps.setInt(3, entry.getValue());
                ps.addBatch();
            }
            result = ps.executeBatch();
            successful = true;
            for (int i = 0; i < result.length; i++) {
                if (result[i] != 1) {
                    successful = false;
                    break;
                }
            }
        } finally {
            closeConnection();
        }
        return successful;
    }

    public List<RequestDTO> searchRequestHistory(String email, String searchResourceName, Timestamp searchFromDate, Timestamp searchToDate) throws Exception {
        List<RequestDTO> result = null;
        RequestDTO request = null;
        int id, index = 1;
        Timestamp fromDate, toDate, sentAt;
        String status;
        String searchString = Generate.generateRequestSearchString(searchResourceName, searchFromDate, searchToDate);
        String sql = "Select ID, SentAt, FromDate, ToDate, dbo.REQUEST_STATUS_NAME(Status) AS Status From " + REQUEST_TABLE_NAME + " Where RequestedUser = ? AND Status != 4 " + searchString + " ORDER BY FromDate ASC";
        Logging.logDebug(logger, "sql", sql);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(index++, email);
            if (searchFromDate != null && searchToDate != null) {
                ps.setTimestamp(index++, searchFromDate);
                ps.setTimestamp(index++, searchToDate);
            } else if (searchFromDate != null) {
                ps.setTimestamp(index++, searchFromDate);
            } else if (searchToDate != null) {
                ps.setTimestamp(index++, searchToDate);
            }
            if (searchResourceName != null) {
                ps.setString(index, searchResourceName);
            }
            rs = ps.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getInt("ID");
                sentAt = rs.getTimestamp("SentAt");
                fromDate = rs.getTimestamp("FromDate");
                toDate = rs.getTimestamp("ToDate");
                status = rs.getString("Status");
                request = new RequestDTO(fromDate, toDate, sentAt, id, status);
                result.add(request);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public List<RequestDTO> searchRequest(String searchKeyword, int searchStatus, int page) throws Exception {
        List<RequestDTO> result = null;
        RequestDTO request = null;
        int id, index = 1;
        Timestamp fromDate, toDate, sentAt, searchDate;
        String status, requestedUser;
        String searchString = Generate.generateRequestSearchString(searchKeyword, searchStatus);
        String sql = "Select ID, SentAt, FromDate, ToDate, RequestedUser, dbo.REQUEST_STATUS_NAME(Status) AS Status From " + REQUEST_TABLE_NAME + " Where Status != 4 " + searchString + " ORDER BY SentAt DESC OFFSET ? ROWS FETCH NEXT " + PAGE_STEP + " ROWS ONLY";
        Logging.logDebug(logger, "sql", sql);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (searchStatus > 0 && searchStatus != 4) {
                ps.setInt(index++, searchStatus);
            }
            if (searchKeyword != null) {
                ps.setString(index++, searchKeyword);
                ps.setString(index++, searchKeyword);
                if (searchKeyword.matches("\\d{4}-(0[1-9]|1[0-2])-((0([1-9]))|((1|2)[0-9])|(3[0-1]))")) {
                    searchDate = Generate.generateTimestamp(searchKeyword);
                    Logging.logDebug(logger, "timestamp", searchDate);
                    ps.setTimestamp(index++, searchDate);
                }
            }
            ps.setInt(index, (page - 1) * PAGE_STEP);
            rs = ps.executeQuery();
            result = new ArrayList<>();
            while (rs.next()) {
                id = rs.getInt("ID");
                sentAt = rs.getTimestamp("SentAt");
                fromDate = rs.getTimestamp("FromDate");
                toDate = rs.getTimestamp("ToDate");
                status = rs.getString("Status");
                requestedUser = rs.getString("RequestedUser");
                request = new RequestDTO(fromDate, toDate, sentAt, id, status, requestedUser);
                result.add(request);
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public int getPagesAmount(String searchKeyword, int searchStatus) throws Exception {
        int index = 1;
        double pagesAmount = 0;
        Timestamp searchDate;
        String searchString = Generate.generateRequestSearchString(searchKeyword, searchStatus);
        String sql = "Select Count(ID) As Count From " + REQUEST_TABLE_NAME + " Where Status != 4 " + searchString;
        Logging.logDebug(logger, "sql", sql);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            if (searchStatus > 0 && searchStatus != 4) {
                ps.setInt(index++, searchStatus);
            }
            if (searchKeyword != null) {
                ps.setString(index++, searchKeyword);
                ps.setString(index++, searchKeyword);
                if (searchKeyword.matches("\\d{4}-(0[1-9]|1[0-2])-((0([1-9]))|((1|2)[0-9])|(3[0-1]))")) {
                    searchDate = Generate.generateTimestamp(searchKeyword);
                    Logging.logDebug(logger, "timestamp", searchDate);
                    ps.setTimestamp(index++, searchDate);
                }
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

    public RequestDTO loadRequestDetail(int id) throws Exception {
        RequestDTO request = null;
        HashMap<ResourceDTO, Integer> detail = null;
        Timestamp fromDate, toDate, sentAt;
        String status, requestedUser;
        int resourceID, resourceAmount, resourceAvailabilityAmount;
        String resourceName;
        String sql1 = "Select SentAt, FromDate, ToDate, RequestedUser, dbo.REQUEST_STATUS_NAME(Status) AS Status From " + REQUEST_TABLE_NAME + " Where Status != 4 AND ID = ?";
        String sql2 = "Select r.ID, r.Name, rd.Amount, dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(r.ID, ?, ?) AS AvailableAmount From " + REQUEST_DETAIL_TABLE_NAME + " rd INNER JOIN " + ResourceDAO.TABLE_NAME + " r ON rd.ResourceID = r.ID Where rd.RequestID = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                sentAt = rs.getTimestamp("SentAt");
                fromDate = rs.getTimestamp("FromDate");
                toDate = rs.getTimestamp("ToDate");
                status = rs.getString("Status");
                requestedUser = rs.getString("RequestedUser");
                request = new RequestDTO(fromDate, toDate, sentAt, id, status, requestedUser);
                ps = conn.prepareStatement(sql2);
                ps.setTimestamp(1, fromDate);
                ps.setTimestamp(2, toDate);
                ps.setInt(3, id);
                rs = ps.executeQuery();
                detail = new HashMap<>();
                while (rs.next()) {
                    resourceID = rs.getInt("ID");
                    resourceName = rs.getString("Name");
                    resourceAvailabilityAmount = rs.getInt("AvailableAmount");
                    ResourceDTO resource = new ResourceDTO(resourceID, resourceAvailabilityAmount, resourceName);
                    resourceAmount = rs.getInt("Amount");
                    detail.put(resource, resourceAmount);
                }
                request.setDetail(detail);
            }
        } finally {
            closeConnection();
        }
        return request;
    }

    public boolean inactivateRequest(int id, String email) throws Exception {
        boolean successful = false;
        String sql = "Update " + REQUEST_TABLE_NAME + " Set Status = 4 Where ID = ? AND RequestedUser = ?";
        Logging.logDebug(logger, "id", id);
        Logging.logDebug(logger, "email", email);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, email);
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }

    public boolean deleteRequest(int id, String processedManager) throws Exception {
        boolean successful = false;
        String sql = "Update " + REQUEST_TABLE_NAME + " Set Status = 3, ProcessedManager = ? Where ID = ? AND Status = 1";
        Logging.logDebug(logger, "id", id);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, processedManager);
            ps.setInt(2, id);
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }

    public boolean acceptRequest(int id, String processedManager) throws Exception {
        boolean successful = false;
        String sql = "Update " + REQUEST_TABLE_NAME + " Set Status = 2, ProcessedManager = ? Where ID = ? AND Status = 1";
        Logging.logDebug(logger, "id", id);
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, processedManager);
            ps.setInt(2, id);
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }
}
