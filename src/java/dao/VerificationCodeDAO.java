/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.VerificationCodeDTO;
import utilities.DBConnection;

/**
 *
 * @author Mk
 */
public class VerificationCodeDAO extends DAO {

    public static final String TABLE_NAME = "VerificationCode";

    public boolean createCode(VerificationCodeDTO code) throws Exception {
        boolean successful = false;
        String sql = "Insert Into " + TABLE_NAME + "(AccountEmail, VerificationCode, CreatedAt) values(?, ?, CURRENT_TIMESTAMP)";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code.getAccountEmail());
            ps.setString(2, code.getVerificationCode());
            successful = ps.executeUpdate() > 0;
        } finally {
            closeConnection();
        }
        return successful;
    }

    public boolean checkCode(VerificationCodeDTO code) throws Exception {
        boolean valid = false;
        String sql = "Select dbo.CHECK_VERIFICATION_CODE(?,?) As Result";
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code.getAccountEmail());
            ps.setString(2, code.getVerificationCode());
            rs = ps.executeQuery();
            if (rs.next()) {
                valid = rs.getBoolean("Result");
            }
        } finally {
            closeConnection();
        }
        return valid;
    }
}
