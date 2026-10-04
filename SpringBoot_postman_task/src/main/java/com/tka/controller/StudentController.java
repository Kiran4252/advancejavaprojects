package com.tka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tka.response.UploadResponse;
import com.tka.service.ExcelService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final ExcelService excelService;

    public StudentController(ExcelService excelService) {
        this.excelService = excelService;
    }
    
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/upload-excel")
    public ResponseEntity<?> uploadExcel(
            @RequestParam("file") MultipartFile file) {

        try {

            // File missing
            if (file == null || file.isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body("File is missing or empty");
            }

            // File extension validation
            String fileName = file.getOriginalFilename();

            if (fileName == null
                    || !fileName.toLowerCase().endsWith(".xlsx")) {

                return ResponseEntity
                        .badRequest()
                        .body("Only .xlsx files are allowed");
            }

            UploadResponse response =
                    excelService.processExcel(file);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to process Excel file: "
                            + e.getMessage());
        }
    }
}