package com.example.demo.controllers;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.auth.AuthLoginDto;
import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.services.CategoryService;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<ResponseObject> create(
            @RequestBody @Valid CategoryDto categoryDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        CategoryDto categoryDto1 = categoryService.createCategory(categoryDto, email);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDto1)
                .message("created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> update(
            @PathVariable("id") int categoryId,
            @RequestBody @Valid CategoryDto categoryDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        CategoryDto categoryDto1 = categoryService.updateCategory(categoryId, categoryDto, email);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDto1)
                .message("created")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        List<CategoryDto> categoryDtos = categoryService.getAllCategrory(email);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDtos)
                .message("success")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseObject> getAllByKey(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        String email = userDetails.getUsername();
        List<CategoryDto> categoryDtos = categoryService.getAllCategroryByKey(email, keyword);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(categoryDtos)
                .message("success")
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
