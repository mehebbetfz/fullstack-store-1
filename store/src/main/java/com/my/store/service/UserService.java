package com.my.store.service;

import com.my.store.dto.RegisterDto;
import com.my.store.model.User;
import com.my.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(RegisterDto dto) {
        // TODO: Check user existence

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setRole(User.Role.USER);

        userRepository.save(user);
    }
}
