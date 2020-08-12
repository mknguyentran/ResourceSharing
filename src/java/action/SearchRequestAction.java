/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.RequestDAO;
import dao.RequestStatusDAO;
import dto.AccountDTO;
import dto.RequestDTO;
import dto.RequestStatusDTO;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import utilities.Generate;
import utilities.Logging;

/**
 *
 * @author Mk
 */
public class SearchRequestAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(SearchRequestAction.class);
    private static final String USER_HISTORY = "/user/requestHistory.jsp";
    private static final String MANAGER_HOME = "/manager/managerHome.jsp";

    private String searchResourceName, searchFromDateString, searchToDateString, searchKeyword = "";
    private int searchStatus = 0, page = 1, pagesAmount, id = 0;
    private Timestamp searchFromDate = null;
    private Timestamp searchToDate = null;
    private List<RequestDTO> requestList;
    private List<RequestStatusDTO> statusList;
    private RequestDTO request;

    public SearchRequestAction() {
    }

    @Action(value = "searchRequestHistory", results = {
        @Result(name = USER_HISTORY, location = USER_HISTORY)
    })
    public String loadUserRequestHistory() throws Exception {
        RequestDAO rDAO = new RequestDAO();
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        try {
            if (searchFromDateString != null) {
                if (!searchFromDateString.isEmpty()) {
                    searchFromDate = Generate.generateTimestamp(searchFromDateString);
                }
            }
            if (searchToDateString != null) {
                if (!searchToDateString.isEmpty()) {
                    searchToDate = Generate.generateTimestamp(searchToDateString);
                }
            }
            if (searchFromDate != null && searchToDate != null) {
                if (searchFromDate.compareTo(searchToDate) > 0) {
                    Timestamp temp = searchFromDate;
                    searchFromDate = searchToDate;
                    searchToDate = temp;
                    String tempString = searchFromDateString;
                    searchFromDateString = searchToDateString;
                    searchToDateString = tempString;
                }
            }
            requestList = rDAO.searchRequestHistory(account.getEmail(), searchResourceName, searchFromDate, searchToDate);
            if (id != 0) {
                request = rDAO.loadRequestDetail(id);
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return USER_HISTORY;
    }

    @Action(value = "searchRequest", results = {
        @Result(name = MANAGER_HOME, location = MANAGER_HOME)
    })
    public String loadRequest() throws Exception {
        RequestDAO rDAO = new RequestDAO();
        RequestStatusDAO rsDAO = new RequestStatusDAO();
        try {
            pagesAmount = rDAO.getPagesAmount(searchKeyword, searchStatus);
            requestList = rDAO.searchRequest(searchKeyword, searchStatus, page);
            statusList = rsDAO.loadStatusList();
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return MANAGER_HOME;
    }

    public List<RequestDTO> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<RequestDTO> requestList) {
        this.requestList = requestList;
    }

    public String getSearchResourceName() {
        return searchResourceName;
    }

    public void setSearchResourceName(String searchResourceName) {
        this.searchResourceName = searchResourceName;
    }

    public String getSearchFromDateString() {
        return searchFromDateString;
    }

    public void setSearchFromDateString(String searchFromDateString) {
        this.searchFromDateString = searchFromDateString;
    }

    public String getSearchToDateString() {
        return searchToDateString;
    }

    public void setSearchToDateString(String searchToDateString) {
        this.searchToDateString = searchToDateString;
    }

    public Timestamp getSearchFromDate() {
        return searchFromDate;
    }

    public void setSearchFromDate(Timestamp searchFromDate) {
        this.searchFromDate = searchFromDate;
    }

    public Timestamp getSearchToDate() {
        return searchToDate;
    }

    public void setSearchToDate(Timestamp searchToDate) {
        this.searchToDate = searchToDate;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public int getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(int searchStatus) {
        this.searchStatus = searchStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesAmount() {
        return pagesAmount;
    }

    public void setPagesAmount(int pagesAmount) {
        this.pagesAmount = pagesAmount;
    }

    public List<RequestStatusDTO> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<RequestStatusDTO> statusList) {
        this.statusList = statusList;
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
