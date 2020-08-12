/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import org.apache.log4j.Logger;

/**
 *
 * @author Mk
 */
public class Logging{

    public static void logError(Logger logger, Exception e) {
        String error = "";
        error += e.getMessage() + "\n";
        for (int i = 0; i < e.getStackTrace().length; i++) {
            error += (e.getStackTrace()[i] + "\n");
        }
        logger.error(error);
    }
    
    public static <T> void logDebug(Logger logger, String message, T value){
        String error = message + ": " + value;
        logger.debug(error);
    }
}
