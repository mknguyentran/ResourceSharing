/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CategoryDTO;
import java.util.ArrayList;
import java.util.List;
import utilities.DBConnection;

/**
 *
 * @author Mk
 */
public class CategoryDAO extends DAO {

    public static final String TABLE_NAME = "ResourceCategory";

    public List<CategoryDTO> loadCategoryList() throws Exception {
        List<CategoryDTO> categoryList = null;
        CategoryDTO category = null;
        int id;
        String name;
        String sql = "Select ID, Name From " + TABLE_NAME;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            categoryList = new ArrayList<>();
            while (rs.next()){
                id = rs.getInt("ID");
                name = rs.getString("Name");
                category = new CategoryDTO(id, name);
                categoryList.add(category);
            }
        } finally {
            closeConnection();
        }
        return categoryList;
    }
}
