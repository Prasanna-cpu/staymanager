package com.spring.staymanager.service.abstraction;

import com.spring.staymanager.dto.UserDTO;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

//    List<UserDTO> getUsersBookingHistory(String userId);

    UserDTO getUserById(String userId);

    void deleteUser(String userId);

    UserDTO getCurrentUserInfo(String email);

}
