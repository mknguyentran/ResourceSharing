/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.Serializable;
import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


/**
 *
 * @author Mk
 */
public class DBConnection implements Serializable{
    public static Connection getConnection() throws Exception{
        Connection conn = null;
        Context context = new InitialContext();
        Context tomcatContext = (Context)context.lookup("java:comp/env");
        DataSource ds = (DataSource)tomcatContext.lookup("ResourceSharing");
        conn = ds.getConnection();
        return conn;
    }
}
