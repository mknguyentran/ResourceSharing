/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author Mk
 */
public class ResourceDTO implements Serializable{
    private int id, quantity, availableAmount;
    private String name, category, color;
    private boolean sufficient = false;

    public ResourceDTO(int id, int quantity, String name, String category, String color) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.category = category;
        this.color = color;
    }

    public ResourceDTO(int id, int quantity, int availableAmount, String name, String category, String color) {
        this.id = id;
        this.quantity = quantity;
        this.availableAmount = availableAmount;
        this.name = name;
        this.category = category;
        this.color = color;
    }

    public ResourceDTO(int id, String name, String category, String color) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color = color;
    }

    public ResourceDTO(int id, int availableAmount, String name) {
        this.id = id;
        this.availableAmount = availableAmount;
        this.name = name;
    }

    public boolean isSufficient() {
        return sufficient;
    }

    public void setSufficient(boolean sufficient) {
        this.sufficient = sufficient;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
