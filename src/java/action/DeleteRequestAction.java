/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.RequestDAO;
import dto.AccountDTO;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class DeleteRequestAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(DeleteRequestAction.class);
    private static final String USER_HISTORY = "searchRequestHistory";
    private static final String LOGOUT = "logout";

    private String searchResourceName, searchFromDate, searchToDate;
    private int id;

    public DeleteRequestAction() {
    }

    @Action(value = "invalidateRequest", results = {
        @Result(name = USER_HISTORY, type = "redirectAction", location = USER_HISTORY)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String execute() {
        String result = LOGOUT;
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        Logging.logDebug(logger, "email", account.getEmail());
        try {
            if (account != null) {
                RequestDAO rDAO = new RequestDAO();
                if (rDAO.inactivateRequest(id, account.getEmail())) {
                    result = USER_HISTORY;
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return result;
    }

    public String getSearchResourceName() {
        return searchResourceName;
    }

    public void setSearchResourceName(String searchResourceName) {
        this.searchResourceName = searchResourceName;
    }

    public String getSearchFromDate() {
        return searchFromDate;
    }

    public void setSearchFromDate(String searchFromDate) {
        this.searchFromDate = searchFromDate;
    }

    public String getSearchToDate() {
        return searchToDate;
    }

    public void setSearchToDate(String searchToDate) {
        this.searchToDate = searchToDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
}
