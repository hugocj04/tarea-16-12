package com.tarea1612.demo.model;

import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"instructor_id", "title"})
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseStatus status = CourseStatus.DRAFT;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @OrderBy("orderNumber ASC")
    private List<Lesson> lessons;

    @ManyToMany
    @JoinTable(
            name = "course_category",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course")
    private List<Review> reviews;

    public void publish() {
        if (lessons.isEmpty()) {
            throw new IllegalStateException("Course must have at least one lesson to be published");
        }
        this.status = CourseStatus.PUBLISHED;
    }

    public void addLesson(Lesson lesson) {
        if (this.status == CourseStatus.ARCHIVED) {
            throw new IllegalStateException("Cannot add lessons to an archived course");
        }

        boolean orderExists = this.lessons.stream()
                .anyMatch(l -> l.getOrderNumber().equals(lesson.getOrderNumber()));

        if (orderExists) {
            throw new IllegalArgumentException("Order number already exists in this course");
        }

        lesson.setCourse(this);
        this.lessons.add(lesson);
    }

    public boolean isAvailableForEnrollment() {
        return this.status == CourseStatus.PUBLISHED;
    }

}
