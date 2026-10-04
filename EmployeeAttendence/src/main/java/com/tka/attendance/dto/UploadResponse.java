package com.tka.attendance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadResponse {

    private String message;

    private int totalRows;

    private int insertedCount;

    private int failedCount;

    private List<RowError> errors;
}