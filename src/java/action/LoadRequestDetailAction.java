/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;
import dao.RequestDAO;
import dto.RequestDTO;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class LoadRequestDetailAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(LoadRequestDetailAction.class);
    private static final String REQUEST_DETAIL = "/manager/requestDetail.jsp";

    private int id;
    private RequestDTO request;

    public LoadRequestDetailAction() {
    }

    @Action(value = "loadRequestDetail", results = {
        @Result(name = REQUEST_DETAIL, location = REQUEST_DETAIL)
    })
    public String loadRequestDetail() {
        RequestDAO rDAO = new RequestDAO();
        try {
            request = rDAO.loadRequestDetail(id);
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return REQUEST_DETAIL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RequestDTO getRequest() {
        return request;
    }

    public void setRequest(RequestDTO request) {
        this.request = request;
    }

}
