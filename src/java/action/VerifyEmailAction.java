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
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class VerifyEmailAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(VerifyEmailAction.class);
    private static final String USER = "searchResource";
    private static final String MANAGER = "searchRequest";
    private static final String INPUT = "input";
    private String code;

    public VerifyEmailAction() {
    }

    @Action(value = "verifyEmail", results = {
        @Result(name = "input", location = "/verifyEmail.jsp")
        ,
        @Result(name = USER, type = "redirectAction", location = USER),
        @Result(name = MANAGER, type = "redirectAction", location = MANAGER)
    })
    public String execute() {
        String label = INPUT;
        VerificationCodeDAO vDAO = new VerificationCodeDAO();
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        VerificationCodeDTO verificationCode = new VerificationCodeDTO(account.getEmail(), code);
        try {
            if (vDAO.checkCode(verificationCode)) {
                AccountDAO aDAO = new AccountDAO();
                if (aDAO.activate(account.getEmail())) {
                    account = aDAO.login(account.getEmail());
                    session.put("ACCOUNT", account);
                    if (account.getRole().equals("employee") || account.getRole().equals("leader")) {
                        label = USER;
                    } else if (account.getRole().equals("manager")){
                        label = MANAGER;
                    }
                }
            } else {
                addFieldError("code", "Invalid code");
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
