package com.example.todo_spring.controller;

import com.example.todo_spring.entity.Task;
import com.example.todo_spring.entity.User;
import com.example.todo_spring.repository.TaskRepository;
import com.example.todo_spring.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Получение текущего пользователя
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @GetMapping("/")
    public String index(Model model) {
        User currentUser = getCurrentUser();
        // Только задачи текущего пользователя
        model.addAttribute("tasks", taskRepository.findByUserOrderByCreatedAtDesc(currentUser));
        model.addAttribute("newTask", new Task());
        return "index";
    }

    @PostMapping("/add")
    public String addTask(@Valid @ModelAttribute("newTask") Task task,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            User currentUser = getCurrentUser();
            model.addAttribute("tasks", taskRepository.findByUserOrderByCreatedAtDesc(currentUser));
            return "index";
        }
        User currentUser = getCurrentUser();
        task.setUser(currentUser);
        taskRepository.save(task);
        return "redirect:/";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTask(@PathVariable("id") Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача не найдена"));
        User currentUser = getCurrentUser();
        if (task.getUser().getId().equals(currentUser.getId())) {
            taskRepository.delete(task);
        }
        return "redirect:/";
    }
}