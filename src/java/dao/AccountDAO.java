/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.AccountDTO;
import utilities.DBConnection;

/**
 *
 * @author Mk
 */
public class AccountDAO extends DAO {

    public final static String TABLE_NAME = "Account";

    public AccountDTO login(String email, String password) throws Exception {
        AccountDTO account = null;
        String name, phoneNumber, address, role, status;
        String sql = "Select Name, PhoneNumber, Address, dbo.ACCOUNTROLENAME(Role) As Role, dbo.ACCOUNTSTATUSNAME(Status) As Status From " + TABLE_NAME + " Where Email = ? AND Password = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("Name");
                phoneNumber = rs.getString("PhoneNumber");
                address = rs.getString("Address");
                role = rs.getString("Role");
                status = rs.getString("Status");
                account = new AccountDTO(email, name, phoneNumber, address, role, status);
            }
        } finally {
            closeConnection();
        }
        return account;
    }
    
    public AccountDTO login(String email) throws Exception {
        AccountDTO account = null;
        String name, phoneNumber, address, role, status;
        String sql = "Select Name, PhoneNumber, Address, dbo.ACCOUNTROLENAME(Role) As Role, dbo.ACCOUNTSTATUSNAME(Status) As Status From " + TABLE_NAME + " Where Email = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                name = rs.getString("Name");
                phoneNumber = rs.getString("PhoneNumber");
                address = rs.getString("Address");
                role = rs.getString("Role");
                status = rs.getString("Status");
                account = new AccountDTO(email, name, phoneNumber, address, role, status);
            }
        } finally {
            closeConnection();
        }
        return account;
    }

    public boolean isExisted(String email) throws Exception {
        boolean isExisted = false;
        String sql = "Select Email From " + TABLE_NAME + " Where Email = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                isExisted = true;
            }
        } finally {
            closeConnection();
        }
        return isExisted;
    }

    public boolean signUp(AccountDTO account, String password) throws Exception {
        boolean successful = false;
        String sql = "Insert Into " + TABLE_NAME + "(Email, Password, Name, PhoneNumber, Address, Role, CreatedAt, Status) values(?, ?, ?, ?, ?, dbo.ACCOUNTROLEID(?), CURRENT_TIMESTAMP, dbo.ACCOUNTSTATUSID(?))";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, account.getEmail());
            ps.setString(2, password);
            ps.setString(3, account.getName());
            ps.setString(4, account.getPhoneNumber());
            ps.setString(5, account.getAddress());
            ps.setString(6, "employee");
            ps.setString(7, "new");
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }

    public boolean activate(String email) throws Exception {
        boolean successful = false;
        String sql = "Update " + TABLE_NAME + " Set Status = dbo.ACCOUNTSTATUSID(?) Where Email = ?";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "active");
            ps.setString(2, email);
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }
    
    
}
