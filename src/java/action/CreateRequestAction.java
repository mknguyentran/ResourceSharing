/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import dao.CategoryDAO;
import dao.ResourceDAO;
import dto.AccountDTO;
import dto.CategoryDTO;
import dto.RequestDTO;
import dto.ResourceDTO;
import java.sql.Timestamp;
import java.util.HashMap;
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
public class CreateRequestAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(CreateRequestAction.class);
    private static final String NEW_REQUEST = "/user/newRequest.jsp";
    private static final String LOGOUT = "logout";

    private String fromDateString, toDateString, searchName = "", error;
    private int searchCategory = 0, pagesAmount, page = 1, id;
    private List<ResourceDTO> resourceList;
    private List<CategoryDTO> categoryList;
    private HashMap<ResourceDTO, Integer> displayCart;

    public CreateRequestAction() {
    }

    @Action(value = "newRequestStep1", results = {
        @Result(name = NEW_REQUEST, location = NEW_REQUEST)
    })
    public String newRequest() {
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        if (request == null) {
            request = new RequestDTO();
        } else {
            request.setStep(1);
            request.clear();
        }
        session.put("REQUEST", request);
        return NEW_REQUEST;
    }

    @Action(value = "newRequestStep2", results = {
        @Result(name = NEW_REQUEST, location = NEW_REQUEST)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String loadResourceAndCart() {
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        try {
            if (request != null) {
                ResourceDAO rDAO = new ResourceDAO();
                CategoryDAO cDAO = new CategoryDAO();
                AccountDTO account = (AccountDTO) session.get("ACCOUNT");
                if (request.getStep() == 1) {
                    if (!fromDateString.isEmpty() && !toDateString.isEmpty()) {
                        Timestamp fromDate = Generate.generateTimestamp(fromDateString);
                        Timestamp toDate = Generate.generateTimestamp(toDateString);
                        Timestamp today = new Timestamp(System.currentTimeMillis());
                        today.setHours(0);
                        today.setMinutes(0);
                        today.setSeconds(0);
                        today.setNanos(0);
                        if (fromDate.compareTo(today) < 0) {
                            error = "Dates must not be in the past";
                        } else if (fromDate.compareTo(toDate) > 0) {
                            error = "From date must not be after To date";
                        } else {
                            request.setFromDate(fromDate);
                            request.setToDate(toDate);
                            resourceList = rDAO.searchResource("", fromDate, toDate, 0, account.getRole(), 1);
                            pagesAmount = rDAO.getPagesAmount("", fromDate, toDate, 0, account.getRole());
                            categoryList = cDAO.loadCategoryList();
                            request.setStep(2);
                            session.put("REQUEST", request);
                            displayCart = rDAO.loadDisplayCart(request, account.getRole());
                        }
                    } else {
                        error = "Please enter both field";
                    }
                } else if (request.getStep() == 2) {
                    resourceList = rDAO.searchResource(searchName, request.getFromDate(), request.getToDate(), searchCategory, account.getRole(), page);
                    pagesAmount = rDAO.getPagesAmount(searchName, request.getFromDate(), request.getToDate(), searchCategory, account.getRole());
                    categoryList = cDAO.loadCategoryList();
                    displayCart = rDAO.loadDisplayCart(request, account.getRole());
                }
            } else {
                return LOGOUT;
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return NEW_REQUEST;
    }

    @Action(value = "addToRequest", results = {
        @Result(name = NEW_REQUEST, location = NEW_REQUEST)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String addToRequest() {
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        try {
            if (request != null) {
                ResourceDAO rDAO = new ResourceDAO();
                int availableAmount = rDAO.getAvailableAmount(id, request.getFromDate(), request.getToDate());
                if (availableAmount > 0) {
                    if (request.getCart().containsKey(id)) {
                        if (request.getCart().get(id) + 1 <= availableAmount) {
                            request.add(id);
                        } else {
                            request.getCart().put(id, availableAmount);
                            error = "You have reached the maximum availability of this resource";
                        }
                    } else {
                        request.add(id);
                    }
                }
            } else {
                return LOGOUT;
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        loadResourceAndCart();
        return NEW_REQUEST;
    }
    
    @Action(value = "decreaseFromRequest", results = {
        @Result(name = NEW_REQUEST, location = NEW_REQUEST)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String decreaseFromRequest() {
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        String result = LOGOUT;
        try {
            if (request != null) {
                if(request.decrease(id)){
                    result = NEW_REQUEST;
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        loadResourceAndCart();
        return result;
    }
    
    @Action(value = "removeFromRequest", results = {
        @Result(name = NEW_REQUEST, location = NEW_REQUEST)
        ,
        @Result(name = LOGOUT, type = "redirect", location = LOGOUT)
    })
    public String removeFromRequest() {
        Map session = ActionContext.getContext().getSession();
        RequestDTO request = (RequestDTO) session.get("REQUEST");
        String result = LOGOUT;
        try {
            if (request != null) {
                if(request.remove(id)){
                    result = NEW_REQUEST;
                }
            }
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        loadResourceAndCart();
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<ResourceDTO, Integer> getDisplayCart() {
        return displayCart;
    }

    public void setDisplayCart(HashMap<ResourceDTO, Integer> displayCart) {
        this.displayCart = displayCart;
    }

    public String getFromDateString() {
        return fromDateString;
    }

    public void setFromDateString(String fromDateString) {
        this.fromDateString = fromDateString;
    }

    public String getToDateString() {
        return toDateString;
    }

    public void setToDateString(String toDateString) {
        this.toDateString = toDateString;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(int searchCategory) {
        this.searchCategory = searchCategory;
    }

    public int getPagesAmount() {
        return pagesAmount;
    }

    public void setPagesAmount(int pagesAmount) {
        this.pagesAmount = pagesAmount;
    }

    public List<ResourceDTO> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<ResourceDTO> resourceList) {
        this.resourceList = resourceList;
    }

    public List<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
