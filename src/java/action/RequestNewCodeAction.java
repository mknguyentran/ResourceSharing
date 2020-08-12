/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
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
public class RequestNewCodeAction extends ActionSupport {

    private static final String VERIFY_EMAIL = "verifyEmail";
    private static final String ERROR = "error";
    private static final Logger logger = Logger.getLogger(RequestNewCodeAction.class);
    private String alert;

    public RequestNewCodeAction() {
    }

    @Action(value = "requestNewCode", results = {
        @Result(name = VERIFY_EMAIL, location = "/verifyEmail.jsp")
        ,
        @Result(name = ERROR, location = "/error.jsp")
    })
    public String execute() {
        String label = ERROR;
        try {
            Map session = ActionContext.getContext().getSession();
            AccountDTO account = (AccountDTO) session.get("ACCOUNT");
            if (account != null) {
                VerificationCodeDAO vDAO = new VerificationCodeDAO();
                String verificationCode = Email.sendVerificationEmail(account.getEmail(), account.getName());
                VerificationCodeDTO code = new VerificationCodeDTO(account.getEmail(), verificationCode);
                if (vDAO.createCode(code)) {
                    alert = "A new verification code is sent to " + account.getEmail();
                    label = VERIFY_EMAIL;
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return label;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

}
