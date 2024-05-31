package com.emailService.Services.ExcelFileProcess;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Data
@Slf4j
public class excelFile {


    public List<List<String>> upload(MultipartFile file) throws Exception {

        List<List<String>> allCellValues = new ArrayList<>();
//
        Path tempDir = Files.createTempDirectory("");

        File tempfile = tempDir.resolve(file.getOriginalFilename()).toFile();

        file.transferTo(tempfile);



        try {
            // Transfer the uploaded file to the temporary file


            // Create a workbook from the temporary file
            try (Workbook workbook = WorkbookFactory.create(tempfile)) {

                // Get the first sheet
                Sheet sheet = workbook.getSheetAt(0);

                // Stream through the rows and process each cell
                Stream<Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);
                rowStream.forEach(row -> {
                    Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(), false);
                    List<String> cellVals = cellStream.map(cell -> {
                        // Handle different cell types appropriately
                        switch (cell.getCellType()) {
                            case STRING:
                                return cell.getStringCellValue();
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    return cell.getDateCellValue().toString();
                                } else {
                                    return String.valueOf(cell.getNumericCellValue());
                                }
                            case BOOLEAN:
                                return String.valueOf(cell.getBooleanCellValue());
                            case FORMULA:
                                return cell.getCellFormula();
                            case BLANK:
                                return "";
                            default:
                                return "Unknown Cell Type";
                        }
                    }).collect(Collectors.toList());

                    allCellValues.add(cellVals);
                });

            }

        } catch (IOException e) {
            throw new Exception("Failed to process uploaded file", e);
        } finally {
            // Ensure the temporary file is deleted after processing
            if (tempfile.exists()) {
                if (!tempfile.delete()) {
                    System.err.println("Failed to delete temporary file: " + tempfile.getAbsolutePath());
                }
            }

        }
        log.info("Sending excel file data: " + new Date());
        return allCellValues;
    }

    //
//        Workbook workbook = WorkbookFactory.create(tempfile);
//
//        Sheet sheetAt = workbook.getSheetAt(0);
//
//        Stream<Row> rowStream = StreamSupport.stream(sheetAt.spliterator(),false);
//
//        rowStream.forEach(row ->{
//            //given a row,get cellStream from it
//            Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(),false);
//
//            List<String> cellVals= cellStream.skip(1).map(cell ->{
//                String cellVal = cell.getStringCellValue();
//                emails.add(cellVal);
//                return cellVal;
//            }).collect(Collectors.toList());
//
//
//        });
//
//        for (String email :emails){
//            System.out.println(email);
//        }
//        System.out.println(emails);
}
