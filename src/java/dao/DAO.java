/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mk
 */
public class DAO implements Serializable{
    protected Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;
    
    protected void closeConnection() throws SQLException{
        if(rs!=null){
            rs.close();
        }
        if(ps!=null){
            ps.close();
        }
        if(conn!=null){
            conn.close();
        }
    }
}
