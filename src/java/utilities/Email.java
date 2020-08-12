/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Mk
 */
public class Email {

    private final static Logger logger = Logger.getLogger(Email.class);

    public static String sendVerificationEmail(String receipentEmail, String name) throws Exception {
        String verificationCode = null;
        final String username = "resourcesharing.noreply21@gmail.com";
        final String password = "pbepcytfonfqgvpe";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        verificationCode = Generate.generateVerificationCode();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("resourcesharing.noreply21@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receipentEmail));
            message.setSubject("Verification Code for Resource Sharing Account");
            message.setText("Hello " + name + ",\n\nYour verification code is: " + verificationCode + " \n\nThe code is going to expired in 5 minutes. \nIf you have not register an account for Resource Sharing recently, please ignore this message. \n\nHave a nice day, \nResource Sharing.");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return verificationCode;
    }
}
