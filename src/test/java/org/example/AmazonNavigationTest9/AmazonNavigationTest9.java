/**
 *Updated Amazon Bot: Implemented DataProvider,Aggregator Pattern and Cron readiness,
 * with Page Object Model,Email Alert Class & PriceReduction Checking class integration
 * to make a robust script
 */

package org.example.AmazonNavigationTest9;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import javax.mail.*;
import java.io.*;
import java.time.Duration;
import java.util.List;

public class AmazonNavigationTest9 {

    WebDriver driver;
    WebDriverWait wait;

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

    @BeforeClass
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
        double percentDrop = 0.00;
        AmazonProductPage pom = new AmazonProductPage(driver,wait,targetAsin);
        pom.navigateToProduct();
        int priceValue = pom.getProductPrice();
        PriceHistoryManager phm = new PriceHistoryManager(priceValue,targetBrand,targetModel);
        phm.historyManager();
        boolean isPriceReduced = phm.priceReductionCheck();
        if(isPriceReduced == true)
            percentDrop = phm.discountCheck();
        EmailAlert ea = new EmailAlert();
        String product = targetBrand + " " + targetModel;
        if(!(percentDrop == 0.00)) //email only gets send when there is a discount
            ea.sendEmailAlert(percentDrop,product);
    }

    @AfterClass
    public void DestroyEnvironment() {
        //Terminate the session and close the browser completely
        driver.quit();
        System.out.println("Session terminated. Browser closed");
    }
}