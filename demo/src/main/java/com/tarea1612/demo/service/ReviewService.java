package com.tarea1612.demo.service;

import com.tarea1612.demo.model.Course;
import com.tarea1612.demo.model.Review;
import com.tarea1612.demo.model.User;
import com.tarea1612.demo.repository.CourseRepository;
import com.tarea1612.demo.repository.ReviewRepository;
import com.tarea1612.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Review addReview(Long userId, Long courseId, int rating, String comment) {
        if (reviewRepository.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new IllegalStateException("User already reviewed this course");
        }

        User user = userRepository.findById(userId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Review review = new Review();
        review.setUser(user);
        review.setCourse(course);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

}
