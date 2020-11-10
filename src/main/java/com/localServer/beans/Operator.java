package com.localServer.beans;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Operator {
    private long operatorId;
    private String operatorName;
    private String boUserName;
    private String boPassword;
    private ArrayList<String> limits;

    public Operator(long operatorId, String operatorName, String boUserName, String boPassword, ArrayList<String> limits) {
        this.operatorId = operatorId;
        this.operatorName = operatorName;
        this.boUserName = boUserName;
        this.boPassword = boPassword;
        this.limits = limits;
    }

    public Operator() {
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getBoUserName() {
        return boUserName;
    }

    public void setBoUserName(String boUserName) {
        this.boUserName = boUserName;
    }

    public String getBoPassword() {
        return boPassword;
    }

    public void setBoPassword(String boPassword) {
        this.boPassword = boPassword;
    }

    public ArrayList<String> getLimits() {
        return limits;
    }

    public void setLimits(ArrayList<String> limits) {
        this.limits = limits;
    }
}
