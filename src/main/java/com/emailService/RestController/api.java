package com.emailService.RestController;

import com.emailService.Bindings.EmailDetails;
import com.emailService.Bindings.EmailParameters;
import com.emailService.Bindings.EmailRequest;
import com.emailService.Helper.CustomResponse;
import com.emailService.Services.EmailService;
import com.emailService.Services.ExcelFileProcess.excelFile;
import com.emailService.Utils.AttachmentUtils;
import com.emailService.Utils.EmailSendService;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/api/v1/email")

public class api {

    private static final Logger log = LoggerFactory.getLogger(api.class);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private excelFile excelfileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    EmailSendService emailSendService;





    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getMessage());
        return ResponseEntity.ok(CustomResponse.builder().
                message("Email Has been send successfully").
                success(true).
                httpStatus(HttpStatus.OK).
                build());
    }


    @PostMapping("/email-with-file")
    public ResponseEntity<?> sendEmail(@RequestParam String request, @RequestParam("file") MultipartFile file) throws IOException {

        try {
            // Convert JSON string to EmailRequest object
            EmailRequest requestResult = objectMapper.readValue(request, EmailRequest.class);

            // Process the email request and file (uncomment your email service logic)

            emailService.sendEmailwithfile(requestResult.getTo(),
                    requestResult.getSubject(),
                    requestResult.getMessage(),
                    file.getInputStream());


            // Return a custom response
            return ResponseEntity.ok(CustomResponse.builder()
                    .message("Email Has been sent successfully")
                    .success(true)
                    .httpStatus(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request format");
        }

    }

    @PostMapping("/send-emails")
    public ResponseEntity<?> sendEmails(@ModelAttribute EmailParameters emailParameters,
                                        @RequestParam("attachments") MultipartFile[] attachments,
                                        @RequestParam("excelfile") MultipartFile excelfile) throws Exception {


        int NoOfEmails = 2;
        List<List<String>> excelData = excelfileService.upload(excelfile);

        List<String> emails = new ArrayList<>();

        List<EmailDetails> emailDetailsList = new ArrayList<>();

        for (int i = 1; i < excelData.size(); i++) {
            List<String> row = excelData.get(i);
            if (row.size() > 1) {
                String email = row.get(1); // Get the email (assuming it's the second element)
                emails.add(email);        // Add the email to the emails list

                // Assuming you have access to subject and message
                String subject = emailParameters.getSubject(); // Set subject if available
                String message = emailParameters.getMsg(); // Set message if available

                // Convert attachments to a list of strings
                List<String> attachmentList = AttachmentUtils.convertMultipartFilesToList(attachments);

                // Create EmailDetails object and populate
                EmailDetails emailDetails = new EmailDetails();
                emailDetails.setRecipient(email);
                emailDetails.setSubject(subject);
                emailDetails.setMessage(message);
                emailDetails.setAttachments(attachmentList);

                // Add EmailDetails object to emailDetailsList
                emailDetailsList.add(emailDetails);
            }
        }

            final Runnable task = new Runnable() {
                int count = 0;

                @Override
                public void run() {
                    if (count < NoOfEmails) { // Run task 10 times
                        try {
                            emailSendService.queueEmails(emailDetailsList,Long.parseLong(emailParameters.getTime().trim()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        count++;
                        log.info("email-counts:"+ count);
                    } else {
                        scheduler.shutdown(); // Shutdown scheduler after completing the task 10 times
                        log.info("scheduler stops");
                    }
                }
            };

            scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES); // Initial delay 0, repeat every 1 second

        return ResponseEntity.ok("Files processed successfully");
    }


}




