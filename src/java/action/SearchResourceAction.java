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
import dto.ResourceDTO;
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
public class SearchResourceAction extends ActionSupport {

    private static final Logger logger = Logger.getLogger(SearchResourceAction.class);
    private static final String USER_HOME = "userHome";
    private List<ResourceDTO> resourceList;
    private List<CategoryDTO> categoryList;
    private String searchName = "";
    private int searchCategory = 0;
    private String searchFromDateString;
    private String searchToDateString;
    private Timestamp searchFromDate = null;
    private Timestamp searchToDate = null;
    private int page = 1, pagesAmount = 1;

    public SearchResourceAction() {
    }

    @Action(value = "searchResource", results = {
        @Result(name = USER_HOME, location = "/user/userHome.jsp")
    })
    public String execute() {
        ResourceDAO rDAO = new ResourceDAO();
        CategoryDAO cDAO = new CategoryDAO();
        Map session = ActionContext.getContext().getSession();
        AccountDTO account = (AccountDTO) session.get("ACCOUNT");
        String role = account.getRole();
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
            if(searchFromDate != null && searchToDate != null){
                if(searchFromDate.compareTo(searchToDate) > 0){
                    Timestamp temp = searchFromDate;
                    searchFromDate = searchToDate;
                    searchToDate = temp;
                    String tempString = searchFromDateString;
                    searchFromDateString = searchToDateString;
                    searchToDateString = tempString;
                }
            }
            resourceList = rDAO.searchResource(searchName, searchFromDate, searchToDate, searchCategory, role, page);
            pagesAmount = rDAO.getPagesAmount(searchName, searchFromDate, searchToDate, searchCategory, role);
            categoryList = cDAO.loadCategoryList();
        } catch (Exception e) {
            Logging.logError(logger, e);
        }
        return USER_HOME;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        Logging.logDebug(logger, "setName", searchName);
        this.searchName = searchName;
    }

    public int getSearchCategory() {
        return searchCategory;
    }

    public void setSearchCategory(int searchCategory) {
        Logging.logDebug(logger, "setCategory", searchCategory);
        this.searchCategory = searchCategory;
    }

    public String getSearchFromDateString() {
        return searchFromDateString;
    }

    public void setSearchFromDateString(String searchFromDateString) {
        Logging.logDebug(logger, "setFrom", searchFromDateString);
        this.searchFromDateString = searchFromDateString;
    }

    public String getSearchToDateString() {
        return searchToDateString;
    }

    public void setSearchToDateString(String searchToDateString) {
        Logging.logDebug(logger, "setTo", searchToDateString);
        this.searchToDateString = searchToDateString;
    }

}
