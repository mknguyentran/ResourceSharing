/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.RequestStatusDTO;
import java.util.ArrayList;
import java.util.List;
import utilities.DBConnection;

/**
 *
 * @author Mk
 */
public class RequestStatusDAO extends DAO {

    public static final String TABLE_NAME = "RequestStatus";

    public List<RequestStatusDTO> loadStatusList() throws Exception {
        List<RequestStatusDTO> statusList = null;
        RequestStatusDTO status = null;
        int id;
        String name;
        String sql = "Select ID, Name From " + TABLE_NAME + " Where ID != 4";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            statusList = new ArrayList<>();
            while (rs.next()){
                id = rs.getInt("ID");
                name = rs.getString("Name");
                status = new RequestStatusDTO(id, name);
                statusList.add(status);
            }
        } finally {
            closeConnection();
        }
        return statusList;
    }
}
