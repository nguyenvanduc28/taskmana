package com.example.demo.repositories;

import com.example.demo.dto.ILastIdCategory;
import com.example.demo.models.Category;
import com.example.demo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CategoryRepository  extends JpaRepository<Category, Integer> {
    List<Category> findAllByUserIdOrderByCreatedAtDesc(Integer userId);

    Category findByUserId(Integer userId);
    Category findByIdAndUserId(Integer categoryId, Integer userId);
    @Query(value = "SELECT MAX(v.id) as lastId FROM category v", nativeQuery = true)
    ILastIdCategory getLastSetId();

    @Query(value = "SELECT * FROM category WHERE user_id = :userId AND LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Category> searchCategoryByKey(@Param("userId") Integer userId, @Param("keyword") String keyword);

}
