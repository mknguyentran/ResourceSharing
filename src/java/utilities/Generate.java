/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Timestamp;
import java.util.Random;

/**
 *
 * @author Mk
 */
public class Generate {

    public static String generateVerificationCode() {
        String code = "";
        Random rand = new Random();
        for (int i = 1; i <= 4; i++) {
            code += rand.nextInt(10);
        }
        return code;
    }

    public static int generateRandomInt(int max) {
        int result = new Random().nextInt(max);
        return result;
    }

    public static String generateResourceSearchString(String searchName, int searchCategory, Timestamp searchFromDate, Timestamp searchToDate, String role) {
        String result = "";
        if (role.equals("leader")) {
            result += " Where (RequestAuthority = dbo.ACCOUNTROLEID('leader') OR RequestAuthority = dbo.ACCOUNTROLEID('employee'))";
        } else if (role.equals("employee")) {
            result += " Where RequestAuthority = dbo.ACCOUNTROLEID('employee')";
        }
//        if (searchFromDate != null || searchToDate != null) {
//            result += " AND dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, ?, ?) > 0";
//        } else {
//            result += " AND dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) > 0";
//        }
        if (searchName != null) {
            result += " AND Name LIKE ?";
        }
        if (searchCategory > 0) {
            result += " AND Category = ?";
        }
        return result;
    }

    public static String generateRequestSearchString(String searchResourceName, Timestamp searchFromDate, Timestamp searchToDate) {
        String result = "";
        if (searchFromDate != null && searchToDate != null) {
            result += " AND CAST(DATEADD(HOUR,7, SentAt) AS DATE) >= ? And CAST(DATEADD(HOUR,7, SentAt) AS DATE) <= ?";
        } else if (searchFromDate != null) {
            result += " AND CAST(DATEADD(HOUR,7, SentAt) AS DATE) >= ?";
        } else if (searchToDate != null) {
            result += " AND CAST(DATEADD(HOUR,7, SentAt) AS DATE) <= ?";
        }
        if (searchResourceName != null) {
            result += " AND ID IN (Select ID From dbo.SEARCH_REQUEST_BY_RESOURCE_NAME(?))";
        }
        return result;
    }

    public static String generateRequestSearchString(String searchKeyword, int searchStatus) {
        String result = "";
        if (searchStatus > 0 && searchStatus != 4) {
            result += " AND Status = ?";
        }
        if (searchKeyword != null) {
            if (searchKeyword.matches("\\d{4}-(0[1-9]|1[0-2])-((0([1-9]))|((1|2)[0-9])|(3[0-1]))")){
                result += " AND (RequestedUser LIKE '%' + ? + '%' OR ID IN (Select ID From dbo.SEARCH_REQUEST_BY_RESOURCE_NAME(?)) OR CAST(DATEADD(HOUR,7, SentAt) AS DATE) = ?)";
            } else {
                result += " AND (RequestedUser LIKE '%' + ? + '%' OR ID IN (Select ID From dbo.SEARCH_REQUEST_BY_RESOURCE_NAME(?)))";
            }
        }
        return result;
    }

    public static String generateAvailableAmountSQL(Timestamp searchFromDate, Timestamp searchToDate) {
        String result = "";
        if (searchFromDate != null || searchToDate != null) {
            result = "dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, ?, ?) AS AvailableAmount";
        } else {
            result = "dbo.CHECK_RESOURCE_AVAILABLE_AMOUNT_IN_RANGE(ID, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) AS AvailableAmount";
        }
        return result;
    }

    public static Timestamp generateTimestamp(String dateString) throws Exception {
        Timestamp result = null;
        int[] dateArray = new int[3];
        String[] data = dateString.split("-");
        for (int i = 0; i < 3; i++) {
            dateArray[i] = Integer.parseInt(data[i]);
        }
        result = new Timestamp(dateArray[0] - 1900, dateArray[1] - 1, dateArray[2], 0, 0, 0, 0);
        return result;
    }
}
