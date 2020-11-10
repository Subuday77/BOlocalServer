package com.localServer.beans;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;


import org.openqa.selenium.support.ui.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class Selenium {

    WebDriver webDriver1;

    public ResponseEntity<?> login(String name, String password) {
        try {
            WebDriver webDriver = driverSetup(webDriver1);
            webDriver1 = webDriver;
            if (webDriver == null) {
                webDriver.quit();
                return new ResponseEntity<>("Window is closed", HttpStatus.GONE);

            }

            webDriver.get("https://boint.tableslive.com/");

            webDriver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(name);
            webDriver.findElement(By.xpath("//*[@id=\"login_tab\"]/tbody/tr[4]/td[2]/input")).sendKeys(password);
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                webDriver.findElement(By.xpath("//*[@id=\"login_tab\"]/tbody/tr[6]/td/input")).click();
            } catch (NoSuchWindowException e) {
                webDriver = null;
                webDriver1 = null;
                webDriver.quit();
                return new ResponseEntity<>("Window is closed1", HttpStatus.GONE);
            }
            if (webDriver.getCurrentUrl().equals("https://boint.tableslive.com/office.php?page=login")) {

                webDriver.quit();
                webDriver1 = null;
                return new ResponseEntity<>("Invalid user or password. Try again.", HttpStatus.FORBIDDEN);


            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (WebDriverException e) {
            if (e.getMessage().contains("no such element")) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else if (!e.getMessage().contains("chrome not reachable")) {
                webDriver1 = null;
                System.out.println(e.getMessage());
                return new ResponseEntity<>("Window is closed2", HttpStatus.GONE);
            }
            webDriver1 = null;
            ResponseEntity responseEntity = login(name, password);
            return responseEntity;
        }
    }

    public ResponseEntity<?> createOperator(Operator operator) {
        try {
            WebDriver webDriver = webDriver1;
            webDriver.get("https://boint.tableslive.com/office.php?action=admin&sub_act=operator_page");
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[1]/td[1]/a/strong")).click();
            webDriver.findElement(By.xpath("//*[@id=\"CustomID\"]")).sendKeys(String.valueOf(operator.getOperatorId()));
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[2]/td[2]/input")).sendKeys(operator.getOperatorName());
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[3]/td[2]/input")).sendKeys(String.valueOf(1));
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[4]/td[2]/input")).sendKeys(String.valueOf(1));
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[5]/td[2]/input")).sendKeys(String.valueOf(1));
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[6]/td[2]/input")).sendKeys(String.valueOf(1));
            Select dropdown = new Select(webDriver.findElement(By.xpath("//*[@id=\"IsActive\"]")));
            dropdown.selectByVisibleText("Active");
            if (webDriver.findElement(By.xpath("//*[@id=\"CustomIDStatus\"]")).getText().contains("Taken By")) {
                return new ResponseEntity<>("Operator ID already taken", HttpStatus.BAD_REQUEST);
            }
            //webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[13]/td[2]/input")).click();

            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication not passed. Try to re-login", HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> tablesManipulations(int[] tablesList, String actionType) {
//        System.out.println(actionType);

try {
    WebDriver webDriver = webDriver1;
        webDriver.get("https://boint.tableslive.com/office.php?action=settings&sub_act=table_mgmt");
    switch (actionType) {
        case "restartAll":
            closeAllTables(webDriver);
            openCloseTables(webDriver, tablesList, "open");
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        case "closeAll":
            closeAllTables(webDriver);
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        case "restart":
            openCloseTables(webDriver, tablesList, "close");
            openCloseTables(webDriver, tablesList, "open");
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        case "close":
            openCloseTables(webDriver, tablesList, "close");
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        case "open":
            openCloseTables(webDriver, tablesList, "open");
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
} catch (Exception e) {
    return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
}

        return null;
    }

    private static void closeAllTables(WebDriver webDriver) {

        webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]")).click();
        webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]")).click();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).getText().equalsIgnoreCase("Reset")) {
            webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).click();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            webDriver.findElement(By.xpath("/html/body/div[13]/div[7]/div/button")).click();
        }
    }

    private static void openCloseTables(WebDriver webDriver, int[] tablesList, String action) {
        String state = action.toLowerCase().contains("open") ? "Initialize" : "Reset";
        for (int i : tablesList) {
            webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
                    .clear();
            webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
                    .sendKeys(String.valueOf(i));
            webDriver.findElement(By.xpath("//*[@id=\"filter_submit\"]/div/a")).click();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).getText()
                    .equalsIgnoreCase(state)) {

                webDriver.findElement(By.xpath("//*[@id=\"IsActive2\"]/a")).click();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                webDriver.findElement(By.xpath("/html/body/div[13]/div[7]/div/button")).click();
                webDriver.findElement(By.xpath("//*[@id=\"report_form\"]/div[1]/div/table/tbody/tr/td[2]/input"))
                        .clear();
            }
        }
    }

    private static WebDriver driverSetup(WebDriver webDriver1) {
        WebDriver webDriver = null;
        try {
            WebDriverManager.chromedriver().setup();
            if (webDriver1 == null && webDriver == null) {
                webDriver = new ChromeDriver();
            } else {
                webDriver = webDriver1;
            }
            return webDriver;
        } catch (Exception e) {
            return null;
        }
    }
}

