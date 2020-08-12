/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import utilities.Convert;

/**
 *
 * @author Mk
 */
public class RequestDTO implements Serializable {

    private Timestamp fromDate, toDate, sentAt;
    private HashMap<Integer, Integer> cart;
    private HashMap<ResourceDTO, Integer> detail;
    private int step, id;
    private String status, requestedUser;

    public RequestDTO() {
        this.step = 1;
        this.cart = new HashMap<>();
    }

    public RequestDTO(Timestamp fromDate, Timestamp toDate, Timestamp sentAt, int id, String status) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sentAt = sentAt;
        this.id = id;
        this.status = status;
    }

    public RequestDTO(Timestamp fromDate, Timestamp toDate, Timestamp sentAt, int id, String status, String requestedUser) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.sentAt = sentAt;
        this.id = id;
        this.status = status;
        this.requestedUser = requestedUser;
    }

    public RequestDTO(Timestamp fromDate, Timestamp toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public int getAmountInCart() {
        int amount = 0;
        for (Integer i : cart.values()) {
            amount += i;
        }
        return amount;
    }

    public boolean add(int ID) {
        boolean successful = false;
        int quantity = 1;
        if (this.cart.containsKey(ID)) {
            quantity = this.cart.get(ID) + 1;
        }
        this.cart.put(ID, quantity);
        successful = true;
        return successful;
    }
    
    public boolean decrease(int ID) {
        boolean successful = false;
        if (this.cart.containsKey(ID)) {
            int quantity = this.cart.get(ID) - 1;
            if (quantity > 0) {
                this.cart.put(ID, quantity);
                successful = true;
            } else if (quantity == 0) {
                if (remove(ID)) {
                    successful = true;
                }
            }
        }
        return successful;
    }

    public boolean remove(int ID) {
        boolean successful = false;
        if (this.cart.containsKey(ID)) {
            this.cart.remove(ID);
            successful = true;
        }
        return successful;
    }
    
    public boolean clear(){
        boolean successful = false;
        for (Integer id : cart.keySet()) {
            cart.remove(id);
        }
        return successful;
    }
    
    public String getDisplayFromDate(){
        return Convert.toSimpleDate(fromDate);
    }
    
    public String getDisplayToDate(){
        return Convert.toSimpleDate(toDate);
    }
    
    public String getDisplaySentAt(){
        return Convert.toSimpleDateWithHour(sentAt);
    }

    public Timestamp getFromDate() {
        return fromDate;
    }

    public void setFromDate(Timestamp fromDate) {
        this.fromDate = fromDate;
    }

    public Timestamp getToDate() {
        return toDate;
    }

    public void setToDate(Timestamp toDate) {
        this.toDate = toDate;
    }

    public HashMap<Integer, Integer> getCart() {
        return cart;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestedUser() {
        return requestedUser;
    }

    public void setRequestedUser(String requestedUser) {
        this.requestedUser = requestedUser;
    }

    public HashMap<ResourceDTO, Integer> getDetail() {
        return detail;
    }

    public void setDetail(HashMap<ResourceDTO, Integer> detail) {
        this.detail = detail;
    }
    
}
