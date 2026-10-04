package com.tka.attendance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.tka.attendance.dto.AttendanceSummaryResponse;
import com.tka.attendance.dto.UploadResponse;
import com.tka.attendance.service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    // ------------------------------------------
    // UPLOAD
    // ------------------------------------------

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(
            @RequestParam("file")
            MultipartFile file) throws Exception {

        UploadResponse response =
                attendanceService.uploadAttendance(file);

        return ResponseEntity.ok(response);
    }

    // ------------------------------------------
    // SUMMARY
    // ------------------------------------------

    @GetMapping("/summary")
    public ResponseEntity<AttendanceSummaryResponse> summary(

            @RequestParam String employeeCode,

            @RequestParam int month,

            @RequestParam int year) {

        AttendanceSummaryResponse response =
                attendanceService.getSummary(
                        employeeCode,
                        month,
                        year);

        return ResponseEntity.ok(response);
    }
}