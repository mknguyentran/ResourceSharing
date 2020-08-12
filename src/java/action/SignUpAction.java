/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.AccountDAO;
import dao.VerificationCodeDAO;
import dto.AccountDTO;
import dto.VerificationCodeDTO;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Email;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class SignUpAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(SignUpAction.class);
    private static final String VERIFY_EMAIL = "verifyEmail";
    private static final String ERROR = "error";
    private String email, password, confirmPassword, name, address, phoneNumber, successAlert;
    private AccountDAO aDAO;

    public SignUpAction() {
        aDAO = new AccountDAO();
    }

    @Override
    public void validate() {
        try {
            if (email.isEmpty()) {
                addFieldError("email", "Please enter your email");
            } else if (!email.matches("[\\w.]{3,50}@\\w+((\\.\\w+)+)")) {
                addFieldError("email", "Please enter a valid email address");
            } else if (aDAO.isExisted(email)) {
                addFieldError("email", "This email is registered with an existing account");
            } else {
                if (password.isEmpty()) {
                    addFieldError("password", "Please enter a password");
                } else if (confirmPassword.isEmpty()) {
                    addFieldError("confirmPassword", "Please confirm your password");
                } else if (!confirmPassword.equals(password)) {
                    addFieldError("confirmPassword", "The passwords you entered do not match");
                }
                if (name.isEmpty()) {
                    addFieldError("name", "Please enter your name");
                }
                if (address.isEmpty()) {
                    addFieldError("address", "Please enter your address");
                }
                if (phoneNumber.isEmpty()) {
                    addFieldError("phoneNumber", "Please enter your phone number");
                } else if (!phoneNumber.matches("(\\+(\\d{1,3}))?\\d{9,10}")) {
                    addFieldError("phoneNumber", "Please enter a valid phone number");
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
    }

    @Action(value = "signUp", results = {
        @Result(name = "input", location = "/signUp.jsp")
        ,
        @Result(name = VERIFY_EMAIL, location = "/verifyEmail.jsp")
        ,
        @Result(name = ERROR, location = "/error.jsp")
    })
    public String execute() {
        String label = ERROR;
        try {
            AccountDTO account = new AccountDTO(email, name, phoneNumber, address);
            if (aDAO.signUp(account, password)) {
                VerificationCodeDAO vDAO = new VerificationCodeDAO();
                String verificationCode = Email.sendVerificationEmail(email, name);
                VerificationCodeDTO code = new VerificationCodeDTO(email, verificationCode);
                Map session = ActionContext.getContext().getSession();
                session.put("ACCOUNT", account);
                if (vDAO.createCode(code)) {
                    label = VERIFY_EMAIL;
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return label;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
