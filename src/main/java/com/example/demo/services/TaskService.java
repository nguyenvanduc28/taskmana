package com.example.demo.services;

import com.example.demo.dto.CategoryDto;
import com.example.demo.dto.ICount;
import com.example.demo.dto.ITaskDto;
import com.example.demo.dto.TaskDto;
import com.example.demo.dto.auth.AuthDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Category;
import com.example.demo.models.Task;
import com.example.demo.models.UserEntity;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    private ModelMapper modelMapper = new ModelMapper();
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<TaskDto> getAllTask(int page, int size, String email) {
        int offset = (page -1)*size;
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        List<ITaskDto> itasks = taskRepository.findAllTask(size, offset, user.getId());
        List<TaskDto> taskDtos = Arrays.asList(modelMapper.map(itasks, TaskDto[].class));
        for (TaskDto taskDto : taskDtos) {
            Category category = categoryRepository.findByIdAndUserId(taskDto.getCategory().getId(), user.getId());
            CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
            taskDto.setCategory(categoryDto);
        }
        return taskDtos;
    }
    public List<TaskDto> getAllTaskByKey(int page, int size, String email, String keyword) {
        int offset = (page -1)*size;
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");
        if (keyword.equals("")) {
            List<ITaskDto> itasks = taskRepository.findAllTask(size, offset, user.getId());
            List<TaskDto> taskDtos = Arrays.asList(modelMapper.map(itasks, TaskDto[].class));
            for (TaskDto taskDto : taskDtos) {
                Category category = categoryRepository.findByIdAndUserId(taskDto.getCategory().getId(), user.getId());
                CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
                taskDto.setCategory(categoryDto);
            }
            return taskDtos;
        }
        else {
            List<ITaskDto> itasks = taskRepository.searchTaskByKey(size, offset, user.getId(), keyword);
            List<TaskDto> taskDtos = Arrays.asList(modelMapper.map(itasks, TaskDto[].class));
            for (TaskDto taskDto : taskDtos) {
                Category category = categoryRepository.findByIdAndUserId(taskDto.getCategory().getId(), user.getId());
                CategoryDto categoryDto = modelMapper.map(category, CategoryDto.class);
                taskDto.setCategory(categoryDto);
            }
            return taskDtos;
        }

    }

    public TaskDto createTask(TaskDto taskDto, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");
        
        Task task = modelMapper.map(taskDto, Task.class);

        Category category = categoryRepository.findByIdAndUserId(task.getCategory().getId(), user.getId());
        if (category == null) throw new NotFoundException("category is not found");

        task.setCategory(category);
        task.setUser(user);
        long currentTimeMillis = Instant.now().toEpochMilli();
        BigDecimal currentTime = BigDecimal.valueOf(currentTimeMillis);
        task.setCreatedAt(currentTime);
        Task taskNew = taskRepository.save(task);

        return modelMapper.map(taskNew, TaskDto.class);
    }

    public TaskDto updateTask(Integer taskId, TaskDto taskDto,String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        Task task = taskRepository.findByIdAndUserId(taskId, user.getId());
        if (task == null) throw new NotFoundException("Task is not found");

        Category category = categoryRepository.findByIdAndUserId(taskDto.getCategory().getId(), user.getId());
        if (category == null) throw new NotFoundException("category is not found");

        task.setId(taskId);
        task.setCategory(category);
        task.setUser(user);
        task.setTitle(taskDto.getTitle());
        task.setStatus(taskDto.isStatus());
        task.setCreatedAt(taskDto.getCreatedAt());
        if (task.isStatus()) {
            long currentTimeMillis = Instant.now().toEpochMilli();
            BigDecimal currentTime = BigDecimal.valueOf(currentTimeMillis);
            task.setCompletedAt(currentTime);
        } else task.setCompletedAt(null);
        long currentTimeMillis = Instant.now().toEpochMilli();
        BigDecimal currentTime = BigDecimal.valueOf(currentTimeMillis);
        task.setUpdatedAt(currentTime);
        Task taskNew = taskRepository.save(task);

        return modelMapper.map(taskNew, TaskDto.class);
    }

    public long countTask( String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");


        ICount icount = taskRepository.countById(user.getId());
        return icount.getCountTask();
    }
    public long countTaskByKey (String keyword, String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        ICount icount = taskRepository.countTaskByKey(user.getId(), keyword);
        return icount.getCountTask();
    }
}
