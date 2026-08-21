/*
*Learning basic testng functions and uses.
*/
package org.example;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

public class Sandbox1 {
    String simulatedData;
    @BeforeMethod
    public void setupEnvironment()
    {
        System.out.println("HOOK TRIGGERED: @BeforeMethod - Booting Isolated Environment...");
        simulatedData = "Hello, World!";
    }

    @Test
    public void validateDummyString()
    {
        System.out.println("TestNG Engine is active. Executing isolated test node.");
        System.out.println("TEST NODE ACTIVE: Executing String Validation...");

        //Hard Assertion
        Assert.assertEquals(simulatedData,"Hello, World!");
    }

    @AfterMethod
    public void tearDownEnvironment()
    {
        System.out.println("HOOK TRIGGERED: @AfterMethod - Destroying environment and freeing memory.\n");
    }
}
