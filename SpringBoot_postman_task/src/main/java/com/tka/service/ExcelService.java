package com.tka.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tka.entity.Student;
import com.tka.repository.StudentRepository;
import com.tka.response.UploadResponse;
import com.tka.response.UploadResponse.RowError;

@Service
public class ExcelService {

    private final StudentRepository studentRepository;

    public ExcelService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public UploadResponse processExcel(MultipartFile file) {

        List<RowError> errors = new ArrayList<>();

        int totalRows = 0;
        int insertedCount = 0;

        Set<String> excelEmails = new HashSet<>();
        Set<String> excelMobiles = new HashSet<>();

        try {

            Workbook workbook = WorkbookFactory.create(file.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            // Header row = row 0
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                totalRows++;

                List<String> rowErrors = new ArrayList<>();

                String studentName = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));
                String mobile = getCellValue(row.getCell(2));
                String course = getCellValue(row.getCell(3));
                String city = getCellValue(row.getCell(4));
                String feesValue = getCellValue(row.getCell(5));

                // -------------------------------
                // Student Name Validation
                // -------------------------------

                if (studentName == null || studentName.trim().isEmpty()) {

                    rowErrors.add("Student name is mandatory");

                } else if (studentName.trim().length() < 3) {

                    rowErrors.add("Student name must contain at least 3 characters");
                }

                // -------------------------------
                // Email Validation
                // -------------------------------

                if (email == null || email.trim().isEmpty()) {

                    rowErrors.add("Email is mandatory");

                } else {

                    email = email.trim();

                    if (!email.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                        rowErrors.add("Invalid email format");

                    } else {

                        if (studentRepository.existsByEmail(email)) {
                            rowErrors.add("Email already exists in database");
                        }

                        if (!excelEmails.add(email)) {
                            rowErrors.add("Duplicate email in uploaded Excel file");
                        }
                    }
                }

                // -------------------------------
                // Mobile Validation
                // -------------------------------

                if (mobile == null || mobile.trim().isEmpty()) {

                    rowErrors.add("Mobile number is mandatory");

                } else {

                    mobile = mobile.trim();

                    if (!mobile.matches("\\d{10}")) {

                        rowErrors.add(
                                "Mobile number must contain exactly 10 digits");

                    } else {

                        if (studentRepository.existsByMobile(mobile)) {
                            rowErrors.add(
                                    "Mobile number already exists in database");
                        }

                        if (!excelMobiles.add(mobile)) {
                            rowErrors.add(
                                    "Duplicate mobile number in uploaded Excel file");
                        }
                    }
                }

                // -------------------------------
                // Course Validation
                // -------------------------------

                if (course == null || course.trim().isEmpty()) {

                    rowErrors.add("Course is mandatory");

                } else {

                    course = course.trim();

                    if (!(course.equalsIgnoreCase("Java")
                            || course.equalsIgnoreCase("Python")
                            || course.equalsIgnoreCase("Testing")
                            || course.equalsIgnoreCase("Data Analytics"))) {

                        rowErrors.add(
                                "Course must be Java, Python, Testing, or Data Analytics");
                    }
                }

                // -------------------------------
                // City Validation
                // -------------------------------

                if (city == null || city.trim().isEmpty()) {

                    rowErrors.add("City is mandatory");
                }

                // -------------------------------
                // Fees Validation
                // -------------------------------

                BigDecimal fees = null;

                if (feesValue == null || feesValue.trim().isEmpty()) {

                    rowErrors.add("Fees is mandatory");

                } else {

                    try {

                        fees = new BigDecimal(feesValue.trim());

                        if (fees.compareTo(BigDecimal.ZERO) <= 0) {

                            rowErrors.add("Fees must be greater than 0");
                        }

                    } catch (NumberFormatException e) {

                        rowErrors.add("Fees must be numeric");
                    }
                }

                // -------------------------------
                // Insert valid row
                // -------------------------------

                if (rowErrors.isEmpty()) {

                    Student student = new Student();

                    student.setStudentName(studentName.trim());
                    student.setEmail(email);
                    student.setMobile(mobile);
                    student.setCourse(course);
                    student.setCity(city.trim());
                    student.setFees(fees);
                    student.setCreatedAt(LocalDateTime.now());

                    studentRepository.save(student);

                    insertedCount++;

                } else {

                    String errorEmail =
                            email == null ? "" : email;

                    String errorMobile =
                            mobile == null ? "" : mobile;

                    errors.add(
                            new RowError(
                                    i + 1,
                                    errorEmail,
                                    errorMobile,
                                    rowErrors));
                }
            }

            workbook.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to process Excel file: " + e.getMessage());
        }

        int failedCount = totalRows - insertedCount;

        return new UploadResponse(
                "Excel processing completed",
                totalRows,
                insertedCount,
                failedCount,
                errors);
    }

    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        DataFormatter formatter = new DataFormatter();

        return formatter.formatCellValue(cell).trim();
    }
}