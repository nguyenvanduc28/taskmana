package com.example.demo.repositories;

import com.example.demo.dto.ICount;
import com.example.demo.dto.ILastIdCategory;
import com.example.demo.dto.ILastIdTask;
import com.example.demo.dto.ITaskDto;
import com.example.demo.models.Category;
import com.example.demo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
//    @Query("select t from task t where t.id = :taskId and t.user_id = :userId")
//    Task findById(@Param("id") Integer taskId, @Param("userId") Integer userId);
//
    List<Task> findAllByUserId(Integer userId);
    Task findByUserId(Integer userId);
    Task findByIdAndUserId(Integer taskId, Integer userId);

    @Query(value = "SELECT MAX(v.id) as lastId FROM task v", nativeQuery = true)
    ILastIdTask getLastSetId();
    @Query(value = "SELECT t.id AS id,\n" +
            "       t.title AS title,\n" +
            "       t.status AS status,\n" +
            "       t.created_at AS createdAt,\n" +
            "       t.updated_at AS updatedAt,\n" +
            "       t.category_id AS categoryId,\n" +
            "       t.completed_at AS completedAt\n" +
            "FROM task t\n" +
            "WHERE t.user_id = :userId\n"+
            "ORDER BY t.created_at DESC\n"+
            "LIMIT :size OFFSET :offset", nativeQuery = true)
    List<ITaskDto> findAllTask(@Param("size") int size, @Param("offset") int offset, Integer userId);
    @Query(value = "SELECT t.id AS id,\n" +
            "       t.title AS title,\n" +
            "       t.status AS status,\n" +
            "       t.created_at AS createdAt,\n" +
            "       t.updated_at AS updatedAt,\n" +
            "       t.category_id AS categoryId,\n" +
            "       t.completed_at AS completedAt\n" +
            "FROM task t\n" +
            "WHERE t.user_id = :userId and LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))\n"+
            "ORDER BY t.created_at DESC\n"+
            "LIMIT :size OFFSET :offset", nativeQuery = true)
    List<ITaskDto> searchTaskByKey(@Param("size") int size, @Param("offset") int offset, Integer userId, @Param("keyword") String keyword);


    @Query(value = "select COUNT(*) as countTask\n" +
            "FROM task t\n" +
            "WHERE t.user_id = :userId and LOWER(title) LIKE LOWER(CONCAT('%', :keyword, '%'))\n", nativeQuery = true)
    ICount countTaskByKey(Integer userId, @Param("keyword") String keyword);
    @Query(value = "select COUNT(*) as countTask\n" +
            "FROM task t\n" +
            "WHERE t.user_id = :userId\n", nativeQuery = true)
    ICount countById(Integer userId);
}
