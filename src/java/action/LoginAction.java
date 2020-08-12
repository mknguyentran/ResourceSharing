/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.AccountDAO;
import dto.AccountDTO;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Logging;
import utilities.Validate;

/**
 *
 * @author Mk
 */
public class LoginAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(LoginAction.class);
    private static final String USER_HOME = "userHome";
    private static final String MANAGER_HOME = "managerHome";
    private static final String VERIFY_EMAIL = "verifyEmail";
    private static final String INPUT = "input";
    private static final String ERROR = "error";
    private String email, password;
    private String gRecaptchaResponse;

    public LoginAction() {
    }

    @Override
    public void validate() {
        if (email.isEmpty()) {
            addFieldError("email", "Please enter your ID");
        }
        if (password.isEmpty()) {
            addFieldError("password", "Please enter your password");
        }
        gRecaptchaResponse = ServletActionContext.getRequest().getParameter("g-recaptcha-response");
        try {
            if (!Validate.validateRecaptchaResponse(gRecaptchaResponse)) {
                addFieldError("recaptcha", "Recaptcha Failed");
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
    }

    @Action(value = "login", results = {
        @Result(name = "input", location = "/login.jsp")
        ,
        @Result(name = ERROR, location = "/error.jsp")
        ,
        @Result(name = USER_HOME, type = "redirectAction", location = "searchResource")
        ,
        @Result(name = MANAGER_HOME, type = "redirectAction", location = "searchRequest")
        ,
        @Result(name = VERIFY_EMAIL, location = "/verifyEmail.jsp")
    })
    public String execute() {
        String label = ERROR;
        try {
            AccountDAO aDAO = new AccountDAO();
            AccountDTO account = aDAO.login(email, password);
            if (account != null) {
                if (account.getStatus().equals("active")) {
                    if (account.getRole().equals("leader") || account.getRole().equals("employee")) {
                        label = USER_HOME;
                    } else if (account.getRole().equals("manager")) {
                        label = MANAGER_HOME;
                    }
                } else if (account.getStatus().equals("deactive")) {
                    addFieldError("email", "This account is deactivated");
                    return INPUT;
                } else if (account.getStatus().equals("new")) {
                    label = VERIFY_EMAIL;
                }
                Map session = ActionContext.getContext().getSession();
                session.put("ACCOUNT", account);
            } else {
                addFieldError("email", "Invalid email or password");
                label = INPUT;
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

}
