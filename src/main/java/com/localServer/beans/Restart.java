package com.localServer.beans;

import org.springframework.stereotype.Component;

@Component
public class Restart {
    private int[] tablesList;
    private String actionType;

    public Restart(int[] tablesList, String actionType) {
        this.tablesList = tablesList;
        this.actionType = actionType;
    }

    public Restart() {
    }

    public int[] getTablesList() {
        return tablesList;
    }

    public void setTablesList(int[] tablesList) {
        this.tablesList = tablesList;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
