package com.tarea1612.demo.service;

import com.tarea1612.demo.model.Course;
import com.tarea1612.demo.model.Lesson;
import com.tarea1612.demo.model.User;
import com.tarea1612.demo.repository.CourseRepository;
import com.tarea1612.demo.repository.EnrollmentRepository;
import com.tarea1612.demo.repository.LessonRepository;
import com.tarea1612.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private LessonRepository lessonRepository;

    public Course createCourseWithLessons(Long instructorId, String title, String description, List<Lesson> lessons) {
        User instructor = userRepository.findById(instructorId).orElseThrow();
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setInstructor(instructor);
        course = courseRepository.save(course);

        for (Lesson lesson : lessons) {
            lesson.setCourse(course);
            lessonRepository.save(lesson);
        }

        return course;
    }
}
