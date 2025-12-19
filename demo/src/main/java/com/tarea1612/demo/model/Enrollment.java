package com.tarea1612.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime enrolledAt;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private int progressPercent;

    public void updateProgress(int newProgress) {
        if (this.status == EnrollmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update progress of a cancelled enrollment");
        }

        if (newProgress < this.progressPercent) {
            throw new IllegalStateException("Progress cannot decrease");
        }

        if (newProgress < 0 || newProgress > 100) {
            throw new IllegalStateException("Progress must be between 0 and 100");
        }
        
        this.progressPercent = newProgress;

        if (newProgress == 100) {
            this.status = EnrollmentStatus.COMPLETED;
        }
    }

    public void markAsCompleted() {
        if (this.progressPercent < 100) {
            throw new IllegalStateException("Cannot mark as completed with progress less than 100%");
        }
        this.status = EnrollmentStatus.COMPLETED;
    }

    public void cancel() {
        this.status = EnrollmentStatus.CANCELLED;
    }
}
