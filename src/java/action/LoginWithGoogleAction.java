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
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Generate;
import utilities.Logging;
import utilities.Validate;

/**
 *
 * @author Mk
 */
public class LoginWithGoogleAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(LoginWithGoogleAction.class);

    private static final String USER_HOME = "searchResource";
    private static final String MANAGER_HOME = "searchRequest";
    private static final String INPUT = "input";
    private String token;

    public LoginWithGoogleAction() {
    }

    @Action(value = "loginWithGoogle", results = {
        @Result(name = "input", type = "redirect", location = "/login.jsp"),
        @Result(name = USER_HOME, type = "redirectAction", location = USER_HOME),
        @Result(name = MANAGER_HOME, type = "redirectAction", location = MANAGER_HOME)
    })
    public String execute() {
        String label = INPUT;
        try {
            Logging.logDebug(logger, "token", token);
            AccountDTO account = Validate.validateGoogleToken(token);
            if (account != null) {
                AccountDAO aDAO = new AccountDAO();
                if (aDAO.isExisted(account.getEmail())) {
                    account = aDAO.login(account.getEmail());
                    if (account.getStatus().equals("new")) {
                        aDAO.activate(account.getEmail());
                        account = aDAO.login(account.getEmail());
                    }
                    if (account.getStatus().equals("active")) {
                        if (account.getRole().equals("leader") || account.getRole().equals("employee")) {
                            label = USER_HOME;
                        } else if (account.getRole().equals("manager")) {
                            label = MANAGER_HOME;
                        }
                    } else if (account.getStatus().equals("deactive")) {
                        addFieldError("email", "This account is deactivated");
                        return INPUT;
                    }
                } else {
                    int randInt = Generate.generateRandomInt(token.length()-30);
                    if(aDAO.signUp(account, token.substring(randInt, randInt + 30))){
                        account = aDAO.login(account.getEmail());
                        label = USER_HOME;
                    }
                }
                Map session = ActionContext.getContext().getSession();
                session.put("ACCOUNT", account);
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return label;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
