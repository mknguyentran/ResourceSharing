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

/**
 *
 * @author Mk
 */
public class ProcessRequestAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(ProcessRequestAction.class);
    private static final String REQUEST_DETAIL = "loadRequestDetail";
    private static final String LOGOUT = "logout";

    private int id;
    private String error;

    public ProcessRequestAction() {
    }

    @Action(value = "deleteRequest", results = {
        @Result(name = REQUEST_DETAIL, type = "chain", location = REQUEST_DETAIL)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String delete() throws Exception {
        String result = LOGOUT;
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        RequestDAO rDAO = new RequestDAO();
        if (rDAO.deleteRequest(id, account.getEmail())) {
            result = REQUEST_DETAIL;
        }
        return result;
    }

    @Action(value = "acceptRequest", results = {
        @Result(name = REQUEST_DETAIL, type = "chain", location = REQUEST_DETAIL)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String accept() throws Exception {
        String result = LOGOUT;
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        RequestDAO rDAO = new RequestDAO();
        if (rDAO.isSufficient(id)) {
            if (rDAO.acceptRequest(id, account.getEmail())) {
                result = REQUEST_DETAIL;
            }
        } else {
            error = "This request can not be fulfilled";
            result = REQUEST_DETAIL;
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
