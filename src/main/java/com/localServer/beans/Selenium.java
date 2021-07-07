package com.localServer.beans;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;


import org.openqa.selenium.support.ui.Select;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


import java.util.Scanner;
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
            webDriver.get("https://boint.tableslive.com/office.php?action=admin&sub_act=operator_page"); //create operator
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
                return new ResponseEntity<>("Operator ID already taken", HttpStatus.IM_USED);
            }
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table/tbody/tr[14]/td[2]/input")).click();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            webDriver.get("https://boint.tableslive.com/office.php?action=admin&sub_act=bo_user_page"); // create bo user
            webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/table[1]/tbody/tr[1]/td[2]/input")).sendKeys(operator.getBoUserName());
            webDriver.findElement(By.xpath("//*[@id=\"newpass1\"]")).sendKeys(operator.getBoPassword());
            webDriver.findElement(By.xpath("//*[@id=\"newpass2\"]")).sendKeys(operator.getBoPassword());

            char[] operatorId = String.valueOf(operator.getOperatorId()).toCharArray();
            while (true) {
                webDriver.findElement(By.xpath("//*[@id=\"OperatorDisplay\"]")).clear();
                for (char number : operatorId) {
                    webDriver.findElement(By.xpath("//*[@id=\"OperatorDisplay\"]")).sendKeys(String.valueOf(number));
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (webDriver.findElement(By.xpath("/html/body/div[4]/form/table[1]/tbody/tr[4]/td[2]/table/tbody/tr/td/table/tbody/tr/td/select/option[1]")).getText().contains(String.valueOf(operator.getOperatorId()))) {
                    webDriver.findElement(By.xpath("/html/body/div[4]/form/table[1]/tbody/tr[4]/td[2]/table/tbody/tr/td/table/tbody/tr/td/select/option[1]")).click();
                    break;
                }
            }
            dropdown = new Select(webDriver.findElement((By.xpath("//*[@id=\"PermissionTypeID\"]"))));
            dropdown.selectByVisibleText("Jedi");
            dropdown.selectByVisibleText("Custom");
            dropdown = new Select(webDriver.findElement(By.xpath("//*[@id=\"ConversionCode\"]")));
            dropdown.selectByVisibleText("EUR");
            webDriver.findElement(By.xpath("/html/body/div[4]/form/table[1]/tbody/tr[15]/td[3]/div/input[1]")).sendKeys(Keys.SPACE);
            dropdown = new Select(webDriver.findElement(By.xpath("//*[@id=\"TempTypeID\"]")));
            dropdown.selectByVisibleText("INT Layer");
            JavascriptExecutor executor = (JavascriptExecutor) webDriver;
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[25]/td[2]/div/label"))); //Outgoing Financial Messages
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"outgoing_bets\"]"))); //Outgoing Financial Messages
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[28]/td[2]/div/label"))); //Roulette Radar
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"roulette_radar\"]"))); //Roulette Radar
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[20]/td[1]/div/label"))); //Late Bets Reports
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"late_bet\"]"))); //Late Bets Reports
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[13]/td[1]/div/label"))); //Games
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"game_rounds\"]"))); //Games
           executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[20]/td/div/label"))); //Settings

//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"settings\"]"))); //Settings
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[20]/td/div/label"))); //Settings
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"settings\"]"))); //Settings
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[61]/td/div/label"))); //Admin
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"admin\"]"))); //Admin
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[61]/td/div/label"))); //Admin
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"admin\"]"))); //Admin
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[87]/td[1]/div/label"))); //Change Password
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"change_password\"]"))); //Change Password
//        executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("/html/body/div[4]/form/table[2]/tbody/tr/td[1]/table/tbody/tr/td/table/tbody/tr[106]/td[1]/div/label"))); //Api Access
            executor.executeScript("arguments[0].click();", webDriver.findElement(By.xpath("//*[@id=\"api_access\"]"))); //Api Access


            webDriver.findElement(By.xpath("/html/body/div[4]/form/table[1]/tbody/tr[16]/td[2]/input")).click();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


//            Scanner sc = new Scanner(System.in);
//            int i = sc.nextInt();


            webDriver.get("https://boint.tableslive.com/office.php?action=settings&sub_act=operator_config_old"); //config apache
            webDriver.switchTo().frame(webDriver.findElement(By.xpath("/html/body/div[4]/iframe")));
            webDriver.findElement(By.xpath("/html/body/div[1]/div[2]")).click();
            webDriver.findElement(By.xpath("//*[@id=\"operator-config\"]/input")).sendKeys(String.valueOf(operator.getOperatorId()));
            dropdown = new Select(webDriver.findElement(By.xpath("//*[@id=\"operator-config\"]/select")));
            dropdown.selectByVisibleText("10457001");
            webDriver.findElement(By.xpath("//*[@id=\"operator-config\"]/button")).click();
            Alert alert = webDriver.switchTo().alert();
            alert.accept();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webDriver.get("https://boint.tableslive.com/office.php?action=settings&sub_act=add_limits"); //limits
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            for (String limit : operator.getLimits()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Select dropdown1 = new Select(webDriver.findElement(By.xpath("//*[@id=\"currency_from\"]")));
                dropdown = new Select(webDriver.findElement(By.xpath("//*[@id=\"currency_to\"]")));
                webDriver.findElement(By.xpath("/html/body/div[4]/form/div/table/tbody/tr[2]/td[2]/input")).sendKeys("13000014");
                webDriver.findElement(By.xpath("/html/body/div[4]/form/div/table/tbody/tr[3]/td[2]/input")).sendKeys(String.valueOf(operator.getOperatorId()));
                dropdown1.selectByVisibleText(limit);
                dropdown.selectByVisibleText(limit);
                webDriver.findElement(By.xpath("//*[@id=\"center\"]/form/div/table/tbody/tr[5]/td/input")).click();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            webDriver.quit();
            webDriver1 = null;
            return new ResponseEntity<HttpStatus>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
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
            //  WebDriverManager.firefoxdriver().setup();
            if (webDriver1 == null && webDriver == null) {
                webDriver = new ChromeDriver();
                //   webDriver = new FirefoxDriver();
            } else {
                webDriver = webDriver1;
            }
            return webDriver;
        } catch (Exception e) {
            return null;
        }
    }

    public static void scrollToTop(WebDriver driver) {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0,0)");
    }

    public static void scrollToBottom(WebDriver driver) {
        ((JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public static void scrollToElementTop(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);", element);
    }

    public static void scrollToElementBottom(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(false);", element);
    }
}

