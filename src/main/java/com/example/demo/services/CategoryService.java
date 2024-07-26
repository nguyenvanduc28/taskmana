package com.example.demo.services;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ILastIdCategory;
import com.example.demo.dto.auth.AuthDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Category;
import com.example.demo.models.UserEntity;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<CategoryDto> getAllCategrory(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        List<Category> categories = categoryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        return Arrays.asList(modelMapper.map(categories, CategoryDto[].class));
    }

    public List<CategoryDto> getAllCategroryByKey(String email, String keyword) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");
        if (keyword.equals("")) {
            List<Category> categories = categoryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
            return Arrays.asList(modelMapper.map(categories, CategoryDto[].class));
        }
        List<Category> categories = categoryRepository.searchCategoryByKey(user.getId(), keyword);

        return Arrays.asList(modelMapper.map(categories, CategoryDto[].class));
    }

    public CategoryDto createCategory(CategoryDto categoryDto, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        Category category = modelMapper.map(categoryDto, Category.class);

        category.setUser(user);
        long currentTimeMillis = Instant.now().toEpochMilli();
        BigDecimal currentTime = BigDecimal.valueOf(currentTimeMillis);
        category.setCreatedAt(currentTime);

        Category categoryNew = categoryRepository.save(category);
        return modelMapper.map(categoryNew, CategoryDto.class);
    }

    public CategoryDto updateCategory(Integer categoryId,CategoryDto categoryDto, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        Category category = categoryRepository.findByIdAndUserId(categoryId, user.getId());
        if (category == null) throw new NotFoundException("category is not found");

        category.setId(categoryId);
        category.setName(categoryDto.getName());
        category.setUser(user);
        long currentTimeMillis = Instant.now().toEpochMilli();
        BigDecimal currentTime = BigDecimal.valueOf(currentTimeMillis);
        category.setUpdatedAt(currentTime);

        Category categoryNew = categoryRepository.save(category);
        return modelMapper.map(categoryNew, CategoryDto.class);
    }

}
