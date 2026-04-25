package com.example.project4.service;

import com.example.project4.entity.User;
import com.example.project4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

// tạo UserRepository → gán vào biến repo
    @Autowired
    private UserRepository repo;
//Nhận username → gọi database → trả về user tương ứng
    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
//Nhận object User → lưu vào database → trả về user đã lưu
    public User save(User user) {
        return repo.save(user);
    }
}