package com.example.todo_spring.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity // Говорит, что это сущность БД
@Table(name = "tasks") // Имя таблицы
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент (SERIAL)
    private Long id;

    @NotBlank(message = "Название обязательно") // Валидация
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp // Автоматически проставляет дату создания
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Конструкторы, геттеры и сеттеры
    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Геттеры и сеттеры (обязательно!)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}