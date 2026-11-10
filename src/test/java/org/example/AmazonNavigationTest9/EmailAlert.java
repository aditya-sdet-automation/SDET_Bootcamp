/**
 *sends email when price drops
 */
package org.example.AmazonNavigationTest9;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailAlert {
    public void sendEmailAlert(double percentageDrop,String product) {
        Properties properties = new Properties(); //instance of properties class
        properties.put("mail.smtp.auth", "true"); //smtp = simple mail transfer protocol
        properties.put("mail.smtp.starttls.enable", "true"); //tls = enable security
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String botmail = "aditya.tester.2026@gmail.com";
        String botPassword = System.getenv("BOT_PASSWORD");
        String subject = "Hourly Price Alert System";

        //Creating the body of mail
        String emailBody = "PRICE DROPPED BY " + percentageDrop + "%! of product: " + product;

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
