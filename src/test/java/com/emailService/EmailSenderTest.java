package com.emailService;

import com.emailService.Services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    EmailService emailService;

    @Test
    void emailSentTest(){
    System.out.println("emailSentTest");
    emailService.sendEmail("shribinwade.9103@gmail.com","EmailTest","This Test of sending email");

    }
    @Test
    void emailSentTest2(){
        System.out.println("emailSentTest");
        emailService.sendEmailWithHtml("shribinwade.9103@gmail.com","EmailTest","<h1>This Test of sending email<h1>");
    }
    @Test
    void emailSentTest3(){
        System.out.println("emailSentTest");
        emailService.sendEmailwithfile("shribinwade.9103@gmail.com","EmailTest with file","email text",new File("src/main/resources/static/pcimage.jpg"));
    }
    @Test
    void emailSentTest4(){
        File file = new File("src/main/resources/static/pcimage.jpg");
        try{
            InputStream inputStream = new FileInputStream(file);
            emailService.sendEmailwithfile(
                    "shribinwade.9103@gmail.com",
                    "EmailTest with file",
                    "email text",
                    inputStream
            );
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException();
        }

        System.out.println("emailSentTest");

    }
}
