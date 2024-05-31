package com.emailService.Utils;

import com.emailService.Bindings.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TaskScheduler taskScheduler;

























//    private Queue<EmailDetails> recipientQueue = new LinkedList<>();
//    private List<EmailDetails> emailDetailsList = new ArrayList<>();
//    private int emailsPerRecipient;
//    private long batchIntervalInMillis;
//    private int currentBatch = 1;
//    private ScheduledFuture<?> scheduledFuture;
//
//    public void queueEmails(List<EmailDetails> emailDetailsList, int emailsPerRecipient, long batchIntervalInMillis) {
//        this.emailDetailsList = emailDetailsList;
//        this.emailsPerRecipient = emailsPerRecipient;
//        this.batchIntervalInMillis = batchIntervalInMillis;
//
//        // Populate recipientQueue with each recipient
//        for (EmailDetails emailDetails : emailDetailsList) {
//            for (int i = 0; i < emailsPerRecipient; i++) {
//                recipientQueue.offer(emailDetails);
//            }
//        }
//
//        scheduleEmailTask();
//    }
//    private void scheduleEmailTask() {
//        if (scheduledFuture != null) {
//            scheduledFuture.cancel(false);
//        }
//
//        // Schedule sending emails in batches with delay between each batch
//        scheduledFuture = taskScheduler.schedule(this::sendNextEmailBatch, Instant.now().plusMillis(batchIntervalInMillis));
//    }
//    private void sendNextEmailBatch() {
//        // Send emails to each recipient in the queue
//        int emailsSent = 0;
//        while (!recipientQueue.isEmpty() && emailsSent < emailsPerRecipient) {
//            sendEmailToNextRecipient();
//            emailsSent++;
//        }
//        System.out.println("email send"+emailsSent);
//
//        // If all recipients have received their emails, schedule the next batch after batchIntervalInMillis
//        if (recipientQueue.isEmpty()) {
//            currentBatch++;
//            System.out.println("Current Batch"+currentBatch);
//            if (currentBatch <= 3) { // Adjust this condition as per your requirement
//                scheduledFuture = taskScheduler.schedule(this::sendNextEmailBatch, Instant.now().plusMillis(batchIntervalInMillis));
//            }
//        }
//    }
//    private void sendEmailToNextRecipient() {
//        if (!recipientQueue.isEmpty()) {
//            String recipient = recipientQueue.poll();
//
//            // Find next emailDetails for this recipient
//            EmailDetails nextEmail = getNextEmailForRecipient(recipient);
//
//            if (nextEmail != null) {
//                try {
//                    sendEmailWithAttachments(nextEmail);
//                } catch (MessagingException e) {
//                    e.printStackTrace(); // Handle exception as needed
//                }
//            }
//        }
//    }
//    private EmailDetails getNextEmailForRecipient(String recipient) {
//        // Find the next emailDetails for the given recipient
//        for (EmailDetails emailDetails : emailDetailsList) {
//            if (emailDetails.getRecipient().equals(recipient)) {
//                return emailDetails;
//            }
//        }
//        return null;
//    }
//    private void sendEmailWithAttachments(EmailDetails emailDetails) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom("shribinwade.100@gmail.com");
//        helper.setTo(emailDetails.getRecipient());
//        helper.setSubject(emailDetails.getSubject());
//        helper.setText(emailDetails.getMessage(), true);
//
//        for (String attachmentPath : emailDetails.getAttachments()) {
//            File file = new File(attachmentPath);
//            helper.addAttachment(file.getName(), file);
//        }
//
//        mailSender.send(message);
//        System.out.println("Sending email to: " + emailDetails.getRecipient() + " at: " + new Date());
//    }
















//    private Queue<String> recipientQueue = new LinkedList<>();
//    private List<EmailDetails> emailDetailsList = new ArrayList<>();
//    private ScheduledFuture<?> scheduledFuture;
//    private long batchIntervalInMillis;
//    private int emailsPerRecipient;
//
//    public void queueEmails(List<EmailDetails> emailDetailsList, int emailsPerRecipient, long batchIntervalInMillis) {
//        this.emailDetailsList = emailDetailsList;
//        this.emailsPerRecipient = emailsPerRecipient;
//        this.batchIntervalInMillis = batchIntervalInMillis;
//
//        // Populate recipientQueue with each recipient repeated emailsPerRecipient times
//        for (EmailDetails emailDetails : emailDetailsList) {
//            for (int i = 0; i < emailsPerRecipient; i++) {
//                recipientQueue.offer(emailDetails.getRecipient());
//            }
//        }
//
//        scheduleEmailTask();
//    }
//
//    private void scheduleEmailTask() {
//        if (scheduledFuture != null) {
//            scheduledFuture.cancel(false);
//        }
//
//        // Schedule sending batches of emails with delay between each batch
//        scheduledFuture = taskScheduler.scheduleWithFixedDelay(this::sendNextEmailBatch, batchIntervalInMillis);
//    }
//
//    private void sendNextEmailBatch() {
//        // Send up to emailsPerRecipient emails to each recipient
//        for (int i = 0; i < emailsPerRecipient; i++) {
//            sendEmailToNextRecipient();
//        }
//
//        // Schedule next batch if there are more recipients
//        if (!recipientQueue.isEmpty()) {
//            scheduledFuture = taskScheduler.schedule(this::sendNextEmailBatch, Instant.now().plusMillis(batchIntervalInMillis));
//        }
//    }
//
//    private void sendEmailToNextRecipient() {
//        if (!recipientQueue.isEmpty()) {
//            String recipient = recipientQueue.poll();
//
//            // Find next emailDetails for this recipient
//            EmailDetails nextEmail = getNextEmailForRecipient(recipient);
//
//            if (nextEmail != null) {
//                try {
//                    sendEmailWithAttachments(nextEmail);
//                } catch (MessagingException e) {
//                    e.printStackTrace(); // Handle exception as needed
//                }
//            }
//        }
//    }
//
//    private EmailDetails getNextEmailForRecipient(String recipient) {
//        // Find the next emailDetails for the given recipient
//        for (EmailDetails emailDetails : emailDetailsList) {
//            if (emailDetails.getRecipient().equals(recipient)) {
//                return emailDetails;
//            }
//        }
//        return null;
//    }
//
//    private void sendEmailWithAttachments(EmailDetails emailDetails) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        helper.setFrom("shribinwade.100@gmail.com");
//        helper.setTo(emailDetails.getRecipient());
//        helper.setSubject(emailDetails.getSubject());
//        helper.setText(emailDetails.getMessage(), true);
//
//        for (String attachmentPath : emailDetails.getAttachments()) {
//            File file = new File(attachmentPath);
//            helper.addAttachment(file.getName(), file);
//        }
//
//        mailSender.send(message);
//        System.out.println("Sending email to: " + emailDetails.getRecipient() + " at: " + new Date());
//    }
//





    private Queue<EmailDetails> emailQueue = new ConcurrentLinkedQueue<>();
    private ScheduledFuture<?> scheduledFuture;
    private long intervalInMillis;


    public void queueEmails(List<EmailDetails> emailDetailsList, long interval) throws Exception {

        emailQueue.addAll(emailDetailsList);
        this.intervalInMillis = interval;
        try {
            scheduleEmailTask();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void scheduleEmailTask() throws Exception{
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduledFuture = taskScheduler.schedule(this::sendEmails, Instant.now().plusMillis(intervalInMillis));

    }
    @Async
    private void sendEmails() {
        while (!emailQueue.isEmpty()) {
            EmailDetails emailDetails = emailQueue.poll();
            if (emailDetails != null) {
                try {
                    sendEmailWithAttachments(emailDetails);
                } catch (MessagingException e) {
                    e.printStackTrace(); // Handle exception as needed
                }
            }
        }
    }

    @Async
    private void sendEmailWithAttachments(EmailDetails emailDetails) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("shribinwade.100@gmail.com");
        helper.setTo(emailDetails.getRecipient());
        helper.setSubject(emailDetails.getSubject());
        helper.setText(emailDetails.getMessage(),true);

        for (String attachmentPath : emailDetails.getAttachments()) {
            File file = new File(attachmentPath);
            helper.addAttachment(file.getName(), file);
        }

        mailSender.send(message);
        System.out.println("Sending emails at: " + new Date());
    }
}
