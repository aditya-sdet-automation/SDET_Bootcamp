/**
 *Page Object Model for AmazonNavigationTest9
 */

package org.example.AmazonNavigationTest9;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;

public class AmazonProductPage {
    WebDriver driver;
    WebDriverWait wait;
    String targetAsin;
    By priceElementLocator = By.xpath("//span[@class='a-price-whole']");

    AmazonProductPage(WebDriver webDriver, WebDriverWait webDriverWait, String webTargetAsin) throws IOException {
        driver = webDriver;
        wait = webDriverWait;
        targetAsin = webTargetAsin;
    }
    void navigateToProduct(){

        //Creating a direct url of the product page
        String directUrl = "https://www.amazon.in/dp/" + targetAsin;

        //command browser to navigate to product page directly
        System.out.println("Bypassing search.Teleporting directly to: " + directUrl);
        driver.get(directUrl);
    }

    int getProductPrice(){
        //locate the price of product
        WebElement priceElement = wait.until(d -> d.findElement(priceElementLocator));
        String priceValue = priceElement.getText();
        int currentPrice = Integer.parseInt(priceValue.replace(",", ""));
        System.out.println("The current price is: Rupees " + priceValue);
        return currentPrice;
    }
}