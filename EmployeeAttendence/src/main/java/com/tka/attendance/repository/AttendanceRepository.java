package com.tka.attendance.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tka.attendance.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	boolean existsByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);
}
