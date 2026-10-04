package com.tka.attendance.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_employee_date",
            columnNames = {"employee_id", "attendance_date"}
        )
    }
)
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "check_in")
    private LocalTime checkIn;

    @Column(name = "check_out")
    private LocalTime checkOut;

    @Column(
        name = "working_hours",
        precision = 5,
        scale = 2
    )
    private BigDecimal workingHours;

    @Column(name = "late")
    private Boolean late;

    @Column(
        name = "overtime_hours",
        precision = 5,
        scale = 2
    )
    private BigDecimal overtimeHours;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}