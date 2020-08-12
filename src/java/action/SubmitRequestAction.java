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
import dto.RequestDTO;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class SubmitRequestAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(SubmitRequestAction.class);
    private static final String USER_HOME = "searchResource";
    private static final String STEP2 = "newRequestStep2";
    private static final String LOGOUT = "logout";

    public SubmitRequestAction() {
    }

    @Action(value = "submitRequest", results = {
        @Result(name = USER_HOME, type = "redirectAction", location = USER_HOME),
        @Result(name = STEP2, type = "redirectAction", location = STEP2),
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String execute() throws Exception {
        String result = LOGOUT;
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        try {
            if (request != null && account != null) {
                RequestDAO rDAO = new RequestDAO();
                if(rDAO.isSufficient(request, account.getRole())){
                    int requestID = rDAO.createRequest(request, account.getEmail());
                    if(requestID > 0){
                        if(rDAO.addRequestDetail(requestID, request.getCart())){
                            result = USER_HOME;
                            session.remove("REQUEST");
                        }
                    }
                } else {
                    result = STEP2;
                }
            } 
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return result;
    }

}
