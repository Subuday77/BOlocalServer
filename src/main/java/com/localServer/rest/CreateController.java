package com.localServer.rest;

import com.localServer.beans.Operator;
import com.localServer.beans.Selenium;
import com.localServer.beans.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class CreateController {

    @Autowired
    Selenium selenium;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        return selenium.login(user.getUserName(),user.getPassword());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOperator(@RequestBody Operator operator) {
        return selenium.createOperator(operator);
    }

}

