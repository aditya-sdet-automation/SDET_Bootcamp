/**
 *
 */

package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class AmazonNavigationTest8 {

    private WebDriver driver;
    private WebDriverWait wait;
    private String emailBody ="";

    @DataProvider(name = "track_list.csv")
    public Object[][] extractcsvData() throws IOException, CsvException {
        String Data[][];
        CSVReader csvReader = new CSVReader(new FileReader("track_list.csv"));
        List<String[]> allRows = csvReader.readAll();
        int line = allRows.size();
        Data = new String[line][3];
        for (int i = 0; i < line; i++) {
                String[] dataParts = allRows.get(i);
                Data[i][0] = dataParts[0];
                Data[i][1]  = dataParts[1];
                Data[i][2] = dataParts[2];
        }
        return Data;
    }

    @BeforeMethod
    public void SetupEnvironment() {
        System.out.println("Initiating Browser Hijack");
        //BROWSER STEALTH & HEADLESS GHOST MODE
        org.openqa.selenium.firefox.FirefoxOptions options = new org.openqa.selenium.firefox.FirefoxOptions();

        //Make it invisible(Headless)
        options.addArguments("-headless");

        //Anti bot evasion(Remove bot tags)
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);

        // create an instance of WebDriver(This actually opens a firefox window)
        driver = new FirefoxDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test(dataProvider = "track_list.csv")
    public void NavigationTest(String targetBrand, String targetModel, String targetAsin) throws IOException {

        int oldPrice = 0;
        int currentPrice = 0;
        System.out.println("The Bot is preparing to search for: " + targetBrand + " " + targetModel);
        String fullProductName = targetBrand + " " + targetModel;
        String saveFileName = fullProductName.replace(" ", "_");
        String historyFileName = saveFileName + "_history.txt";
        String memoryFileName = saveFileName + "_last_price.txt";

        //Creating a direct url of the product page
        String directUrl = "https://www.amazon.in/dp/" + targetAsin;

        //command browser to navigate to product page directly
        System.out.println("Bypassing search.Teleporting directly to: " + directUrl);
        driver.get(directUrl);

        By priceElementLocator = By.xpath("//span[@class='a-price-whole']");
        WebElement priceElement = wait.until(d -> d.findElement(priceElementLocator));
        String priceValue = priceElement.getText();
        currentPrice = Integer.parseInt(priceValue.replace(",", ""));
        System.out.println("The current price is: Rupees " + priceValue);

        //Sticky note to remember last price for comparison
        File stickyNote = new File(memoryFileName);
        if (stickyNote.exists()) {
            Scanner sc = new Scanner(stickyNote);
            oldPrice = Integer.parseInt(sc.nextLine().replace(",", ""));
            sc.close();
        }

        //Memory Logic
        //get current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = now.format(formatter);

        //format sentence we want to save into file
        String dataRecord = timestamp + " | Target Item Price: ₹" + currentPrice + "\n";

        //The Pen(FileWriter)
        //True is important = tells Java to append to the end of the file
        //if false,Java would erase history every time it runs
        FileWriter fileWriter = new FileWriter(historyFileName, true);
        fileWriter.write(dataRecord);
        fileWriter.close();//always put the cap back on pen!
        System.out.println("Data permanently saved to: " + historyFileName);
        FileWriter fileWriter1 = new FileWriter(memoryFileName);
        fileWriter1.write(String.valueOf(currentPrice));
        fileWriter1.close();
        priceReductionCheck(oldPrice, currentPrice,targetBrand,targetModel);
    }

    @AfterMethod
    public void DestroyEnvironment() {
        //Terminate the session and close the browser completely
        driver.quit();
        System.out.println("Session terminated. Browser closed");
    }

    static double discountCheck(int oldCost, int newCost) {
        double dropAmount = oldCost - newCost;
        double percentageDrop = ((dropAmount) / oldCost) * 100;
        return percentageDrop;
    }

    //check if there is a price reduction
    public void priceReductionCheck(int oldPrice, int cleanPrice, String targetBrand, String targetModel) throws IOException {
        boolean pricelowered = false;
        String priceDropLog = "Price_Drop_Log.txt";
        FileWriter fileWriter = new FileWriter(priceDropLog, true);
        String priceState;
        String product = targetBrand + targetModel;
        if (cleanPrice < oldPrice)
            pricelowered = true;
        if (pricelowered == true) {
            double discount = discountCheck(oldPrice, cleanPrice);
            System.out.println("ALERT, PRICE LOWERED!!!");
            System.out.println("PRICE DROPPED BY " + discount + "%!");
            //add it to a price drop log which gets sent to me on an hourly basis.
            priceState = "PRICE DROPPED BY " + discount + "%! of product: " + product;
            emailBody = emailBody + priceState;
        } else priceState = "There are no changes in current prices of product: " + product;
        fileWriter.write(priceState);
        fileWriter.close();
    }

    @AfterClass
    //sends email when price drops
     public void sendEmailAlert() {
        Properties properties = new Properties(); //instance of properties class
        properties.put("mail.smtp.auth", "true"); //smtp = simple mail transfer protocol
        properties.put("mail.smtp.starttls.enable", "true"); //tls = enable security
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String botmail = "aditya.tester.2026@gmail.com";
        String botPassword = System.getenv("BOT_PASSWORD");
        String subject = "Hourly Price Alert System";

        //creating a session instance == connecting to server
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(botmail, botPassword);
            }
        });

        //sending mail comes under try catch block
        try {
            Message message = new MimeMessage(session);

            //who is it from? converts string email to internetaddress
            message.setFrom(new InternetAddress(botmail));

            //who is it going to?
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("reena69r96@gmail.com"));

            //Subject
            message.setSubject(subject);

            //Body
            message.setText(emailBody);

            //SEND BUTTON
            if(!emailBody.isEmpty()) {
                Transport.send(message);
                System.out.println("EMAIL ALERT SENT SUCCESSFULLY");
            }
            else {
                System.out.println("No price drops today,email cancelled");
            }

        } catch (Exception e) {
            System.out.println("failed to send email. Error: " + e.getMessage());
        }
    }
}