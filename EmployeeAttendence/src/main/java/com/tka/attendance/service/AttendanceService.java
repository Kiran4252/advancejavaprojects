package com.tka.attendance.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tka.attendance.dto.AttendanceSummaryResponse;
import com.tka.attendance.dto.RowError;
import com.tka.attendance.dto.UploadResponse;
import com.tka.attendance.entity.Attendance;
import com.tka.attendance.entity.Employee;
import com.tka.attendance.repository.AttendanceRepository;
import com.tka.attendance.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;

    private static final Set<String> REQUIRED_HEADERS = Set.of(
            "employee_code",
            "employee_name",
            "attendance_date",
            "status",
            "check_in",
            "check_out"
    );

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm");

    // =========================================================
    // UPLOAD ATTENDANCE FILE
    // =========================================================

    public UploadResponse uploadAttendance(MultipartFile file) throws Exception {

        // 1. File validation
        validateFile(file);

        String fileName = file.getOriginalFilename();

        List<RowError> errors = new ArrayList<>();

        int totalRows = 0;
        int insertedCount = 0;

        Set<String> uploadedKeys = new HashSet<>();

        // =====================================================
        // CSV
        // =====================================================

        if (fileName.toLowerCase().endsWith(".csv")) {

            try (InputStream inputStream = file.getInputStream();
                 CSVParser parser = CSVParser.parse(
                         inputStream,
                         java.nio.charset.StandardCharsets.UTF_8,
                         CSVFormat.DEFAULT.builder()
                                 .setHeader()
                                 .setSkipHeaderRecord(true)
                                 .build())) {

                validateCsvHeaders(parser.getHeaderNames());

                for (CSVRecord record : parser) {

                    totalRows++;

                    int rowNumber = (int) record.getRecordNumber() + 1;

                    String employeeCode = getCsvValue(record, "employee_code");
                    String employeeName = getCsvValue(record, "employee_name");
                    String attendanceDate = getCsvValue(record, "attendance_date");
                    String status = getCsvValue(record, "status");
                    String checkIn = getCsvValue(record, "check_in");
                    String checkOut = getCsvValue(record, "check_out");
                    String overtime = getCsvValue(record, "overtime_hours");

                    List<String> rowErrors = validateRow(
                            employeeCode,
                            employeeName,
                            attendanceDate,
                            status,
                            checkIn,
                            checkOut,
                            overtime,
                            uploadedKeys
                    );

                    if (!rowErrors.isEmpty()) {

                        errors.add(
                                new RowError(
                                        rowNumber,
                                        employeeCode,
                                        rowErrors
                                )
                        );

                        continue;
                    }

                    // Save valid row
                    Attendance attendance = createAttendance(
                            employeeCode,
                            employeeName,
                            attendanceDate,
                            status,
                            checkIn,
                            checkOut,
                            overtime
                    );

                    attendanceRepository.save(attendance);

                    insertedCount++;
                }

            }

        }

        // =====================================================
        // XLSX
        // =====================================================

        else if (fileName.toLowerCase().endsWith(".xlsx")) {

            try (InputStream inputStream = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);

                DataFormatter formatter = new DataFormatter();

                if (sheet.getPhysicalNumberOfRows() == 0) {
                    throw new IllegalArgumentException("Excel file is empty");
                }

                // Read headers
                Row headerRow = sheet.getRow(0);

                if (headerRow == null) {
                    throw new IllegalArgumentException("Header row is missing");
                }

                List<String> headers = new ArrayList<>();

                for (int i = 0; i < headerRow.getLastCellNum(); i++) {

                    String header =
                            formatter.formatCellValue(headerRow.getCell(i))
                                    .trim();

                    headers.add(header);
                }

                validateExcelHeaders(headers);

                // Read data rows
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);

                    if (row == null) {
                        continue;
                    }

                    totalRows++;

                    int rowNumber = i + 1;

                    String employeeCode =
                            getExcelValue(row, headers, "employee_code", formatter);

                    String employeeName =
                            getExcelValue(row, headers, "employee_name", formatter);

                    String attendanceDate =
                            getExcelValue(row, headers, "attendance_date", formatter);

                    String status =
                            getExcelValue(row, headers, "status", formatter);

                    String checkIn =
                            getExcelValue(row, headers, "check_in", formatter);

                    String checkOut =
                            getExcelValue(row, headers, "check_out", formatter);

                    String overtime =
                            getExcelValue(row, headers, "overtime_hours", formatter);

                    List<String> rowErrors = validateRow(
                            employeeCode,
                            employeeName,
                            attendanceDate,
                            status,
                            checkIn,
                            checkOut,
                            overtime,
                            uploadedKeys
                    );

                    if (!rowErrors.isEmpty()) {

                        errors.add(
                                new RowError(
                                        rowNumber,
                                        employeeCode,
                                        rowErrors
                                )
                        );

                        continue;
                    }

                    Attendance attendance = createAttendance(
                            employeeCode,
                            employeeName,
                            attendanceDate,
                            status,
                            checkIn,
                            checkOut,
                            overtime
                    );

                    attendanceRepository.save(attendance);

                    insertedCount++;
                }
            }
        }

        int failedCount = errors.size();

        String message;

        if (failedCount == 0) {
            message = "Attendance uploaded successfully";
        } else if (insertedCount > 0) {
            message = "Attendance uploaded with some errors";
        } else {
            message = "Attendance upload failed";
        }

        return new UploadResponse(
                message,
                totalRows,
                insertedCount,
                failedCount,
                errors
        );
    }

    // =========================================================
    // FILE VALIDATION
    // =========================================================

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "File is missing or empty"
            );
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IllegalArgumentException(
                    "File name is missing"
            );
        }

        String lowerName = fileName.toLowerCase();

        if (!lowerName.endsWith(".csv")
                && !lowerName.endsWith(".xlsx")) {

            throw new IllegalArgumentException(
                    "Only CSV and XLSX files are allowed"
            );
        }
    }

    // =========================================================
    // CSV HEADER VALIDATION
    // =========================================================

    private void validateCsvHeaders(List<String> headers) {

        Set<String> headerSet = new HashSet<>(headers);

        for (String requiredHeader : REQUIRED_HEADERS) {

            if (!headerSet.contains(requiredHeader)) {

                throw new IllegalArgumentException(
                        "Missing required header: " + requiredHeader
                );
            }
        }
    }

    // =========================================================
    // EXCEL HEADER VALIDATION
    // =========================================================

    private void validateExcelHeaders(List<String> headers) {

        Set<String> headerSet = new HashSet<>(headers);

        for (String requiredHeader : REQUIRED_HEADERS) {

            if (!headerSet.contains(requiredHeader)) {

                throw new IllegalArgumentException(
                        "Missing required header: " + requiredHeader
                );
            }
        }
    }

    // =========================================================
    // CSV VALUE
    // =========================================================

    private String getCsvValue(
            CSVRecord record,
            String columnName) {

        try {

            String value = record.get(columnName);

            if (value == null) {
                return "";
            }

            return value.trim();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // EXCEL VALUE
    // =========================================================

    private String getExcelValue(
            Row row,
            List<String> headers,
            String columnName,
            DataFormatter formatter) {

        int index = headers.indexOf(columnName);

        if (index == -1) {
            return "";
        }

        if (row.getCell(index) == null) {
            return "";
        }

        return formatter
                .formatCellValue(row.getCell(index))
                .trim();
    }

    // =========================================================
    // ROW VALIDATION
    // =========================================================

    private List<String> validateRow(
            String employeeCode,
            String employeeName,
            String attendanceDate,
            String status,
            String checkIn,
            String checkOut,
            String overtime,
            Set<String> uploadedKeys) {

        List<String> errors = new ArrayList<>();

        // -----------------------------------------------------
        // Employee Code
        // -----------------------------------------------------

        if (employeeCode == null || employeeCode.isBlank()) {

            errors.add("Employee code is required");

        } else {

            OptionalEmployeeResult result =
                    findEmployee(employeeCode);

            if (!result.exists()) {

                errors.add("Employee code does not exist");

            } else {

                Employee employee = result.employee();

                // Employee name must match master
                if (employeeName == null
                        || employeeName.isBlank()) {

                    errors.add("Employee name is required");

                } else if (employeeName.length() < 3) {

                    errors.add(
                            "Employee name must contain at least 3 characters"
                    );

                } else if (!employee.getEmployeeName()
                        .equalsIgnoreCase(employeeName.trim())) {

                    errors.add(
                            "Employee name does not match employee master"
                    );
                }
            }
        }

        // -----------------------------------------------------
        // Date
        // -----------------------------------------------------

        LocalDate date = null;

        if (attendanceDate == null
                || attendanceDate.isBlank()) {

            errors.add("Attendance date is required");

        } else {

            try {

                date = LocalDate.parse(
                        attendanceDate,
                        DATE_FORMAT
                );

                if (date.isAfter(LocalDate.now())) {

                    errors.add(
                            "Attendance date cannot be a future date"
                    );
                }

            } catch (DateTimeParseException e) {

                errors.add(
                        "Invalid attendance date. Use yyyy-MM-dd"
                );
            }
        }

        // -----------------------------------------------------
        // Status
        // -----------------------------------------------------

        if (status == null || status.isBlank()) {

            errors.add("Status is required");

        } else {

            status = status.trim();

            if (!status.equalsIgnoreCase("Present")
                    && !status.equalsIgnoreCase("Absent")
                    && !status.equalsIgnoreCase("Half Day")
                    && !status.equalsIgnoreCase("WFH")) {

                errors.add(
                        "Status must be Present, Absent, Half Day or WFH"
                );
            }
        }

        // -----------------------------------------------------
        // Time validation
        // -----------------------------------------------------

        LocalTime inTime = null;
        LocalTime outTime = null;

        boolean timeRequired =
                status != null
                        && (status.equalsIgnoreCase("Present")
                        || status.equalsIgnoreCase("WFH"));

        if (timeRequired) {

            if (checkIn == null || checkIn.isBlank()) {

                errors.add(
                        "Check-in is required for Present/WFH"
                );

            } else {

                try {

                    inTime = LocalTime.parse(
                            checkIn,
                            TIME_FORMAT
                    );

                } catch (DateTimeParseException e) {

                    errors.add(
                            "Invalid check-in time. Use HH:mm"
                    );
                }
            }

            if (checkOut == null || checkOut.isBlank()) {

                errors.add(
                        "Check-out is required for Present/WFH"
                );

            } else {

                try {

                    outTime = LocalTime.parse(
                            checkOut,
                            TIME_FORMAT
                    );

                } catch (DateTimeParseException e) {

                    errors.add(
                            "Invalid check-out time. Use HH:mm"
                    );
                }
            }
        }

        // -----------------------------------------------------
        // Absent cannot have times
        // -----------------------------------------------------

        if (status != null
                && status.equalsIgnoreCase("Absent")) {

            if ((checkIn != null && !checkIn.isBlank())
                    || (checkOut != null && !checkOut.isBlank())) {

                errors.add(
                        "Absent employee cannot have check-in/check-out"
                );
            }
        }

        // -----------------------------------------------------
        // Checkout must be greater than check-in
        // -----------------------------------------------------

        if (inTime != null && outTime != null) {

            if (!outTime.isAfter(inTime)) {

                errors.add(
                        "Check-out must be greater than check-in"
                );
            }
        }

        // -----------------------------------------------------
        // Working hours
        // -----------------------------------------------------

        double workingHours = 0;

        if (inTime != null && outTime != null) {

            long minutes =
                    Duration.between(inTime, outTime).toMinutes();

            workingHours = minutes / 60.0;

            // Present minimum 4 hours
            if (status.equalsIgnoreCase("Present")
                    && workingHours < 4) {

                errors.add(
                        "Present employee must have at least 4 working hours"
                );
            }
        }

        // -----------------------------------------------------
        // Overtime validation
        // -----------------------------------------------------

        double uploadedOvertime = 0;

        if (overtime != null && !overtime.isBlank()) {

            try {

                uploadedOvertime =
                        Double.parseDouble(overtime);

                if (uploadedOvertime < 0) {

                    errors.add(
                            "Overtime cannot be negative"
                    );
                }

                if (uploadedOvertime > 5) {

                    errors.add(
                            "Overtime cannot be greater than 5 hours"
                    );
                }

            } catch (NumberFormatException e) {

                errors.add(
                        "Overtime must be numeric"
                );
            }
        }

        // -----------------------------------------------------
        // Calculated overtime
        // -----------------------------------------------------

        double calculatedOvertime = 0;

        if (workingHours > 9) {

            calculatedOvertime =
                    workingHours - 9;
        }

        // Uploaded overtime must match calculated overtime
        if (overtime != null && !overtime.isBlank()) {

            if (Math.abs(
                    uploadedOvertime - calculatedOvertime
            ) > 0.25) {

                errors.add(
                        "Uploaded overtime does not match calculated overtime"
                );
            }
        }

        // -----------------------------------------------------
        // Duplicate inside uploaded file
        // -----------------------------------------------------

        if (employeeCode != null
                && !employeeCode.isBlank()
                && date != null) {

            String key =
                    employeeCode.toUpperCase()
                            + "_" + date;

            if (uploadedKeys.contains(key)) {

                errors.add(
                        "Duplicate employee/date in uploaded file"
                );

            } else {

                uploadedKeys.add(key);
            }

            // -------------------------------------------------
            // Duplicate in database
            // -------------------------------------------------

            Employee employee =
                    employeeRepository
                            .findByEmployeeCode(employeeCode)
                            .orElse(null);

            if (employee != null) {

                boolean exists =
                        attendanceRepository
                                .existsByEmployeeIdAndAttendanceDate(
                                        employee.getId(),
                                        date
                                );

                if (exists) {

                    errors.add(
                            "Attendance already exists for employee/date"
                    );
                }
            }
        }

        return errors;
    }

    // =========================================================
    // CREATE ATTENDANCE ENTITY
    // =========================================================

    private Attendance createAttendance(
            String employeeCode,
            String employeeName,
            String attendanceDate,
            String status,
            String checkIn,
            String checkOut,
            String overtime) {

        Employee employee =
                employeeRepository
                        .findByEmployeeCode(employeeCode)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Employee not found"
                                )
                        );

        LocalDate date =
                LocalDate.parse(
                        attendanceDate,
                        DATE_FORMAT
                );

        LocalTime inTime = null;
        LocalTime outTime = null;

        if (checkIn != null
                && !checkIn.isBlank()) {

            inTime =
                    LocalTime.parse(
                            checkIn,
                            TIME_FORMAT
                    );
        }

        if (checkOut != null
                && !checkOut.isBlank()) {

            outTime =
                    LocalTime.parse(
                            checkOut,
                            TIME_FORMAT
                    );
        }

        double workingHours = 0;

        if (inTime != null && outTime != null) {

            long minutes =
                    Duration.between(
                            inTime,
                            outTime
                    ).toMinutes();

            workingHours =
                    minutes / 60.0;
        }

        boolean late = false;

        if (inTime != null) {

            LocalTime lateTime =
                    LocalTime.of(9, 45);

            late = inTime.isAfter(lateTime);
        }

        double calculatedOvertime = 0;

        if (workingHours > 9) {

            calculatedOvertime =
                    workingHours - 9;
        }

        Attendance attendance =
                new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(date);
        attendance.setStatus(status);
        attendance.setCheckIn(inTime);
        attendance.setCheckOut(outTime);

        // IMPORTANT:
        // BigDecimal is used because entity uses
        // @Column(precision = 5, scale = 2)

        attendance.setWorkingHours(
                BigDecimal.valueOf(workingHours)
        );

        attendance.setLate(late);

        attendance.setOvertimeHours(
                BigDecimal.valueOf(calculatedOvertime)
        );

        attendance.setCreatedAt(
                java.time.LocalDateTime.now()
        );

        return attendance;
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    public AttendanceSummaryResponse getSummary(
            String employeeCode,
            int month,
            int year) {

        // Validate month
        if (month < 1 || month > 12) {

            throw new IllegalArgumentException(
                    "Month must be between 1 and 12"
            );
        }

        // Validate year
        if (year < 2000
                || year > LocalDate.now().getYear()) {

            throw new IllegalArgumentException(
                    "Invalid year"
            );
        }

        Employee employee =
                employeeRepository
                        .findByEmployeeCode(employeeCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found"
                                )
                        );

        YearMonth yearMonth =
                YearMonth.of(year, month);

        LocalDate startDate =
                yearMonth.atDay(1);

        LocalDate endDate =
                yearMonth.atEndOfMonth();

        List<Attendance> attendanceList =
                attendanceRepository.findAll();

        int presentDays = 0;
        int absentDays = 0;
        int halfDays = 0;
        int wfhDays = 0;
        int lateCount = 0;

        double totalWorkingHours = 0;
        double totalOvertimeHours = 0;

        for (Attendance attendance : attendanceList) {

            // Employee filter
            if (!attendance.getEmployee()
                    .getId()
                    .equals(employee.getId())) {

                continue;
            }

            // Date filter
            LocalDate attendanceDate =
                    attendance.getAttendanceDate();

            if (attendanceDate.isBefore(startDate)
                    || attendanceDate.isAfter(endDate)) {

                continue;
            }

            String status =
                    attendance.getStatus();

            if (status.equalsIgnoreCase("Present")) {

                presentDays++;

            } else if (status.equalsIgnoreCase("Absent")) {

                absentDays++;

            } else if (status.equalsIgnoreCase("Half Day")) {

                halfDays++;

            } else if (status.equalsIgnoreCase("WFH")) {

                wfhDays++;
            }

            if (Boolean.TRUE.equals(
                    attendance.getLate())) {

                lateCount++;
            }

            if (attendance.getWorkingHours() != null) {

                totalWorkingHours +=
                        attendance.getWorkingHours()
                                .doubleValue();
            }

            if (attendance.getOvertimeHours() != null) {

                totalOvertimeHours +=
                        attendance.getOvertimeHours()
                                .doubleValue();
            }
        }

        AttendanceSummaryResponse response =
                new AttendanceSummaryResponse();

        response.setEmployeeCode(
                employee.getEmployeeCode()
        );

        response.setEmployeeName(
                employee.getEmployeeName()
        );

        response.setMonth(month);
        response.setYear(year);

        response.setPresentDays(presentDays);
        response.setAbsentDays(absentDays);
        response.setHalfDays(halfDays);
        response.setWfhDays(wfhDays);
        response.setLateCount(lateCount);

        response.setTotalWorkingHours(
                totalWorkingHours
        );

        response.setTotalOvertimeHours(
                totalOvertimeHours
        );

        return response;
    }

    // =========================================================
    // EMPLOYEE FIND RESULT
    // =========================================================

    private OptionalEmployeeResult findEmployee(
            String employeeCode) {

        Employee employee =
                employeeRepository
                        .findByEmployeeCode(employeeCode)
                        .orElse(null);

        return new OptionalEmployeeResult(
                employee != null,
                employee
        );
    }

    private record OptionalEmployeeResult(
            boolean exists,
            Employee employee
    ) {
    }
}