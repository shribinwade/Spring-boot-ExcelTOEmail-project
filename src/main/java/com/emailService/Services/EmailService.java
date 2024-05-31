package com.emailService.Services;


import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;


public interface EmailService {

    // send email to single person
    void sendEmail(String to,String subject, String message);

    // send email to multiple person
    void sendEmail(String [] to,String subject, String message);

    // send email with HTML
    void sendEmailWithHtml(String to, String subject, String htmlContent);

    //void send email with file
    void sendEmailwithfile(String to, String subject,String text, File file);

    void sendEmailwithfile(String to, String subject, String message, InputStream inputStream);



}
