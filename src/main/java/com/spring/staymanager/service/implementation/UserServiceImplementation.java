package com.spring.staymanager.service.implementation;

import com.spring.staymanager.dto.UserDTO;
import com.spring.staymanager.entity.User;
import com.spring.staymanager.exception.ObjectNotFoundException;
import com.spring.staymanager.mapper.UserMapper;
import com.spring.staymanager.repository.UserRepository;
import com.spring.staymanager.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = users.stream().map(UserMapper::mapToUserDTO).toList();
        return userDTOS;
    }

    @Override
    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        UserDTO userDTO = UserMapper.mapToUserDTO(user);
        return userDTO;
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete,
                        () -> {
                            throw new ObjectNotFoundException("User not found");
                        });
    }

    @Override
    public UserDTO getCurrentUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ObjectNotFoundException("User not found"));
        UserDTO userDTO = UserMapper.mapToUserDTO(user);
        return userDTO;
    }
}
