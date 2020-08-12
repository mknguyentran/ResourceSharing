/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import dto.AccountDTO;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Mk
 */
public class Validate {

    public static final String RECAPTCHA_VERIFY_SITE = "https://www.google.com/recaptcha/api/siteverify";
    public static final String GOOGLETOKEN_VERIFY_SITE = "https://oauth2.googleapis.com/tokeninfo";
    private static final String SECRET_KEY = "6LeZXasZAAAAAD2zlmiOnNHkoQw50RQPUYV3-_Sk";

    public static boolean validateRecaptchaResponse(String response) throws Exception {
        boolean result = false;
        OutputStream os = null;
        InputStream is = null;
        JsonReader jr = null;
        try {
            if (response != null) {
                if (!response.isEmpty()) {
                    URL url = new URL(RECAPTCHA_VERIFY_SITE);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    String params = "secret=" + SECRET_KEY + "&response=" + response;
                    conn.setDoOutput(true);
                    os = conn.getOutputStream();
                    os.write(params.getBytes());
                    os.flush();
                    is = conn.getInputStream();
                    jr = Json.createReader(is);
                    JsonObject jo = jr.readObject();
                    result = jo.getBoolean("success");
                }
            }
        } finally {
            if (jr != null) {
                jr.close();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            return result;
        }
    }

    public static AccountDTO validateGoogleToken(String token) throws Exception {
        AccountDTO account = null;
        OutputStream os = null;
        InputStream is = null;
        JsonReader jr = null;
        try {
            if (token != null) {
                if (!token.isEmpty()) {
                    String urlWithParam = GOOGLETOKEN_VERIFY_SITE + "?id_token=" + token;
                    URL url = new URL(urlWithParam);
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    is = conn.getInputStream();
                    jr = Json.createReader(is);
                    JsonObject jo = jr.readObject();
                    String email = jo.getString("email");
                    String name = jo.getString("name");
                    account = new AccountDTO(email, name);
                }
            }
        } finally {
            if (jr != null) {
                jr.close();
            }
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            
        }
        return account;
    }
}
