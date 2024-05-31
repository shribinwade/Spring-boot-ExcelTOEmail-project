package com.emailService.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Async
public class AttachmentUtils {

//    public static List<String> convertMultipartFilesToList(MultipartFile[] files) throws IOException {
//        List<String> attachmentList = new ArrayList<>();
//        for (MultipartFile file : files) {
//            // Read the file content as a string and add it to the list
//            String fileContent = new String(file.getBytes(), StandardCharsets.UTF_8);
//            attachmentList.add(fileContent);
//        }
//        return attachmentList;
//    }

    private static final String UPLOAD_DIR = "src/main/resources/attachments/"; // Update this path
    @Async
    public static List<String> convertMultipartFilesToList(MultipartFile[] files) throws IOException {
        List<String> attachmentList = new ArrayList<>();
        for (MultipartFile file : files) {
            // Save the file to the specified directory and add the path to the list
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);
            Files.write(filePath, file.getBytes());
            attachmentList.add(filePath.toString());
        }
        log.info("Sending excel file data: " + new Date());
        return attachmentList;
    }
}
