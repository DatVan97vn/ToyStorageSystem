package com.toystorage.backend.services.auth;

import com.toystorage.backend.dto.request.auth.RegisterRequest;
import com.toystorage.backend.models.auth.User;
import com.toystorage.backend.models.auth.Role;
import com.toystorage.backend.repository.auth.UserRepository;
import com.toystorage.backend.repository.auth.RoleRepository; // Import Repo mới
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.toystorage.backend.enums.users.UserStatus;
import java.util.List;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Inject thêm RoleRepository vào đây
    private final PasswordEncoder passwordEncoder;

    // ================= FIND USER =================
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    // ================= SAVE USER =================
    public User save(User user) {
        return userRepository.save(user);
    }

    // ================= ENCODE PASSWORD =================
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // ================= CHECK PASSWORD =================
    public boolean checkPassword(
            String rawPassword,
            String encodedPassword
    ) {
        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(
            User user,
            String newPassword
    ) {
        user.setPassword(
                passwordEncoder.encode(newPassword)
        );
        user.setFirstLogin(false);
        userRepository.save(user);
    }

    // ================= UPDATE LAST LOGIN =================
    public void updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // ================= SAVE STAFF WITH ROLE =================
    /*
     * Hàm lấy Role thực tế dưới Database lên để mapping khóa ngoại với User Entity
     */
    @Transactional
    public User saveStaffWithRole(User staff, String roleName) {
        // Tìm kiếm quyền hạn trong DB, nếu không có thì bắn lỗi thay vì crash hệ thống
        Role targetRole = roleRepository.findByName(roleName.toUpperCase().trim())
                .orElseThrow(() -> new RuntimeException("Vai trò hệ thống '" + roleName + "' chưa được khởi tạo trong DB!"));

        staff.setRole(targetRole);
        return userRepository.save(staff);
    }

    // ================= CREATE NEW USER =================
    @Transactional
    public User createUser(RegisterRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email '" + request.getEmail() + "' đã được đăng ký trong hệ thống!");
        });

        String hashedPassword = encodePassword(request.getPassword());

        User newUser = User.builder()
                .password(hashedPassword)
                .name(request.getFullName())
                .email(request.getEmail().trim())
                .build();

        newUser.setStatus(com.toystorage.backend.enums.users.UserStatus.ACTIVE);
        newUser.setFirstLogin(true);

        // Map role thông qua dữ liệu lấy lên từ DB cho luồng Builder
        if (request.getRole() != null) {
            Role targetRole = roleRepository.findByName(request.getRole().toUpperCase().trim())
                    .orElseThrow(() -> new RuntimeException("Vai trò '" + request.getRole() + "' không hợp lệ!"));
            newUser.setRole(targetRole);
        }

        return userRepository.save(newUser);
    }
    // ================= GET ALL USERS =================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ================= GET USER BY ID =================
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
    }

    // ================= UPDATE USER STATUS =================
    @Transactional
    public User updateUserStatus(Long id, UserStatus status) {
        User user = getUserById(id);

        user.setStatus(status);

        return userRepository.save(user);
    }

    @Transactional
    public void resetPassword(Long id) {
        User user = getUserById(id);

        user.setPassword(passwordEncoder.encode("123456"));
        user.setFirstLogin(true);

        userRepository.save(user);
    }
}