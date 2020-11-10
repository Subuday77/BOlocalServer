package com.localServer.rest;


import com.localServer.beans.Restart;
import com.localServer.beans.Selenium;
import com.localServer.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
public class TableRestartController {
    @Autowired
    Selenium selenium;

    @PostMapping("/restart")
    public ResponseEntity<?> tablesRestart(@RequestBody Restart restart) {

        return selenium.tablesManipulations(restart.getTablesList(), restart.getActionType());
    }

//    @PostMapping("/open")
//    public ResponseEntity<?> tablesOpen(@RequestBody Integer[] tablesList, String actionType) {
//        return selenium.tablesManipulations(tablesList, actionType);
//    }
//
//    @PostMapping("/close")
//    public ResponseEntity<?> tablesClose(@RequestBody Integer[] tablesList, String actionType) {
//        return selenium.tablesManipulations(tablesList, actionType);
//    }
}
