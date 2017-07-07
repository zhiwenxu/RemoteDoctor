package com.uestcpg.remotedoctor.beans;

import com.uestcpg.remotedoctor.Class.Order;

import java.util.List;

/**
 * Created by dmsoft on 2017/7/6.
 */

public class OrderBean{

    private List<Order> sickOrders;
    private String Success;
    private String Message;

    public List<Order> getOrders() {
        return sickOrders;
    }


    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
    public void setOrders(List<Order> sickOrders) {
        this.sickOrders = sickOrders;
    }
}
