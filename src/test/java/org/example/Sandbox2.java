package org.example;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Sandbox2 {
    @Test
    public void ValidateCartMath()
    {
        // 1. The Raw Data (Simulating driver.findElement(...).getText())
        String uiItem1 = "$15.50";
        String uiItem2 = "$9.25";
        String uiTotal = "Total: $24.75";

        // 2. String Manipulation
        String uiItemClean1 = uiItem1.replaceAll("[^0-9.]","");
        String uiItemClean2 = uiItem2.replaceAll("[^0-9.]","");
        String uiTotalClean = uiTotal.replaceAll("[^0-9.]","");

        // 3. Type Casting
        double uiItemPrice1 = Double.parseDouble(uiItemClean1);
        double uiItemPrice2 = Double.parseDouble(uiItemClean2);
        double uiTotalPrice = Double.parseDouble(uiTotalClean);

        // 4. The Math
        double uiItemPriceTotal = uiItemPrice1+uiItemPrice2;

        // 5. The Assertion
        Assert.assertEquals(uiItemPriceTotal,uiTotalPrice,"FATAL ERROR! MATH IS NOT MATHING" );

        System.out.println("Cart validation successful. Mathematics hold true.");
    }
}
