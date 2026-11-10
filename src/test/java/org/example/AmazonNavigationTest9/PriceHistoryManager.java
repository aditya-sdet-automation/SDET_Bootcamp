/**
*Manages price tracking,if there is a discount and if yes by how much for AmazonNavigationTest9
*/
package org.example.AmazonNavigationTest9;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class PriceHistoryManager {
   private String brand;
   private String model;
   private int oldPrice = 0;
   private int currentPrice = 0;

    PriceHistoryManager(int cleanPrice, String targetBrand,String targetModel) throws IOException {
        currentPrice = cleanPrice;
        brand = targetBrand;
        model = targetModel;
    }

    void historyManager() throws IOException {

        String fullProductName = brand + " " + model;
        System.out.println("The Bot is preparing to search for: " + fullProductName);
        String saveFileName = fullProductName.replace(" ", "_");
        String historyFileName = saveFileName + "_history.txt";
        String memoryFileName = saveFileName + "_last_price.txt";

        //Sticky note to remember last price for comparison
        File stickyNote = new File(memoryFileName);
        if (stickyNote.exists()) {
           try( Scanner sc = new Scanner(stickyNote)){
            oldPrice = Integer.parseInt(sc.nextLine().replace(",", ""));
           }
           catch(Exception e){
               oldPrice = 0;
           }
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
        try(FileWriter fileWriter = new FileWriter(historyFileName, true)){
        fileWriter.write(dataRecord);
        System.out.println("Data permanently saved to: " + historyFileName);}
        catch (Exception e){
            System.out.println("History class failed");
        }
        try(FileWriter fileWriter1 = new FileWriter(memoryFileName)){
        fileWriter1.write(String.valueOf(currentPrice));}
        catch (Exception e){
            System.out.println("Short term memory loss!!!");
        }
    }

    //check if there is a price reduction
    boolean priceReductionCheck() throws IOException {
        boolean priceLowered = false;
        if (currentPrice < oldPrice)
            priceLowered = true;
        return priceLowered;
    }

    //if there is a reduction, how much percent discount is it
    double discountCheck() {
        double dropAmount = oldPrice - currentPrice;
        double percentageDrop = ((dropAmount) / oldPrice) * 100;
        return percentageDrop;
    }
}
