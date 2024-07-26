package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.services.CategoryService;
import com.example.demo.services.TaskService;
import com.example.demo.services.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final CategoryService categoryService;
    private final TaskService taskService;
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<ResponseObject> create(
            @RequestBody @Valid TaskDto taskDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        TaskDto taskDto1 = taskService.createTask(taskDto, email);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(taskDto1)
                .message("created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(
            @PathVariable("id") int taskId,
            @RequestBody @Valid TaskDto taskDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        TaskDto taskDto1 = taskService.updateTask(taskId, taskDto, email);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(taskDto1)
                .message("created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping
    public ResponseEntity<PagedResponseObject> getAllTask(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {

        long totalItems = taskService.countTask(userDetails.getUsername());
        int totalPages = (int) Math.ceil((double) totalItems / size);
        String email = userDetails.getUsername();
        List<TaskDto> taskDtos = taskService.getAllTask(page, size, email);
        MetaObject metaObject = new MetaObject();
        metaObject.setPage(page);
        metaObject.setPerPage(size);
        metaObject.setTotalPages(totalPages);
        metaObject.setTotalItems(totalItems);

        return ResponseEntity.ok(PagedResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(taskDtos)
                .meta(metaObject)
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<PagedResponseObject> searchTaskByKey(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @AuthenticationPrincipal UserDetails userDetails) {

        long totalItems = taskService.countTaskByKey(keyword, userDetails.getUsername());
        int totalPages = (int) Math.ceil((double) totalItems / size);
        String email = userDetails.getUsername();
        List<TaskDto> taskDtos = taskService.getAllTaskByKey(page, size, email, keyword);
        MetaObject metaObject = new MetaObject();
        metaObject.setPage(page);
        metaObject.setPerPage(size);
        metaObject.setTotalPages(totalPages);
        metaObject.setTotalItems(totalItems);

        return ResponseEntity.ok(PagedResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(taskDtos)
                .meta(metaObject)
                .build());
    }
}
