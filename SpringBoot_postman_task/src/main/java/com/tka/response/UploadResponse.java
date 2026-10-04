package com.tka.response;

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
	
	
	@Data
	@AllArgsConstructor
	public static class RowError {
		
		private int row;
		private String email;
		private String mobile;
		private List<String> errors;
		
	}

}
