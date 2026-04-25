package com.example.project4.repository;

import com.example.project4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//UserRepository giúp đọc/ghi dữ liệu User từ database mà không cần viết SQL

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}