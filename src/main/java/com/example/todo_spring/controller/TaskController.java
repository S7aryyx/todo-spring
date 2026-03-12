package com.example.todo_spring.controller;

import com.example.todo_spring.entity.Task;
import com.example.todo_spring.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller // @Controller, а не @RestController, так как мы отдаем HTML
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // Показать главную страницу со списком задач
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("newTask", new Task()); // Пустой объект для формы
        return "index"; // Имя шаблона (index.html)
    }

    // Добавить новую задачу
    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("newTask") Task task,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Если ошибка валидации, показываем форму снова
            model.addAttribute("tasks", taskRepository.findAllByOrderByCreatedAtDesc());
            return "index";
        }
        taskRepository.save(task);
        return "redirect:/"; // Перенаправление на главную
    }

    // Удалить задачу
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
        return "redirect:/";
    }
}