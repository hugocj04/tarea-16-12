package com.tarea1612.demo.service;

import com.tarea1612.demo.model.Course;
import com.tarea1612.demo.model.EnrollmentStatus;
import com.tarea1612.demo.model.Enrollment;
import com.tarea1612.demo.model.User;
import com.tarea1612.demo.repository.CourseRepository;
import com.tarea1612.demo.repository.EnrollmentRepository;
import com.tarea1612.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Enrollment enrollUserInCourse(Long userId, Long courseId) {
        if (enrollmentRepository.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new IllegalStateException("User already enrolled in this course");
        }
        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollment.setProgressPercent(0);

        return enrollmentRepository.save(enrollment);
    }

    public void updateProgress(Long enrollmentId, int progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setProgressPercent(progress);
        if (progress == 100) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
        }
        enrollmentRepository.save(enrollment);
    }
}