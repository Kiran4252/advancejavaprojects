package com.tka.attendance.dto;

import lombok.Data;

@Data
public class AttendanceSummaryResponse {

    private String employeeCode;

    private String employeeName;

    private int month;

    private int year;

    private int presentDays;

    private int absentDays;

    private int halfDays;

    private int wfhDays;

    private int lateCount;

    private double totalWorkingHours;

    private double totalOvertimeHours;
}