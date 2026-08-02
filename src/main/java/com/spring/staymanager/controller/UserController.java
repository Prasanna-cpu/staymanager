package com.spring.staymanager.controller;


import com.spring.staymanager.dto.UserDTO;
import com.spring.staymanager.response.ApiResponse;
import com.spring.staymanager.service.abstraction.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all-users")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsersHandler() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse(
                                userDTOS,
                                "Users fetched successfully",
                                HttpStatus.OK.value(),
                                HttpStatus.OK
                        )
                );
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getUserByIdHandler(
            @PathVariable String userId
    ){
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse(
                                userDTO,
                                "User fetched successfully",
                                HttpStatus.OK.value(),
                                HttpStatus.OK
                        )
                );
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteUserHandler(
            @PathVariable String userId
    ){
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse(
                                null,
                                "User deleted successfully",
                                HttpStatus.OK.value(),
                                HttpStatus.OK
                        )
                );
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getCurrentUserInfoHandler(
            @PathVariable String email
    ){
        UserDTO userDTO = userService.getCurrentUserInfo(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse(
                                userDTO,
                                "Current user info fetched successfully",
                                HttpStatus.OK.value(),
                                HttpStatus.OK
                        )
                );
    }

}
