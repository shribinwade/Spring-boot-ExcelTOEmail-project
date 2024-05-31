package com.emailService.Services;

import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
@Data
@Slf4j
public class EmailServiceImpl implements EmailService {

    public static Logger getLog() {
        return log;
    }


    @Autowired
    private JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }



    @Override
    public void sendEmail(String to, String subject, String message) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("shribinwade.100@gmail.com");
        javaMailSender.send(simpleMailMessage);
        log.info("Email has been send");


    }

    @Override
    public void sendEmail(String[] to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setFrom("shribinwade.100@gmail.com");
        javaMailSender.send(simpleMailMessage);
        log.info("Email has been send");
    }
    @Override
    public void sendEmailWithHtml(String to, String subject, String htmlContent) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("shribinwade.100@gmail.com");
            helper.setText(htmlContent,true);
            javaMailSender.send(mimeMessage);
            log.info("Email With Html send ");


        }catch (Exception ex){
               ex.printStackTrace();
               throw new RuntimeException(ex);
        }

    }

    @Async
    @Override
    public void sendEmailwithfile(String to, String subject,String text, File file) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

            helper.setTo(to);
            helper.setFrom("shribinwade.100@gmail.com");
            helper.setText(text);
            helper.setSubject(subject);

            FileInputStream fileInputStream = new FileInputStream(file);
//            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(), file);
            javaMailSender.send(mimeMessage);
            log.info("Email With file send ");


        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }

    @Async
    @Override
    public void sendEmailwithfile(String to, String subject, String message, InputStream inputStream) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            Integer no=0;
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,"UTF-8");

            helper.setTo(to);
            helper.setFrom("shribinwade.100@gmail.com");
            helper.setText(message,true);
            helper.setSubject(subject);
            File file = new File("src/main/resources/email/test.jpg");
            Files.copy(inputStream,file.toPath(),StandardCopyOption.REPLACE_EXISTING);
//            FileInputStream fileInputStream = new FileInputStream(file);
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(file.getName(),fileSystemResource);
            javaMailSender.send(mimeMessage);
            log.info("Email With file send ");


        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }


    }
}
