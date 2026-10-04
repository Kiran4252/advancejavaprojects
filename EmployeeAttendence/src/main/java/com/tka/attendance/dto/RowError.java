package com.tka.attendance.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RowError {
	
	private int row;
	
	private String employeeCode;
	
	private List<String> errors;

}
