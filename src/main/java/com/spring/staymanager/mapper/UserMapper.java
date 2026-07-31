package com.spring.staymanager.mapper;

import com.spring.staymanager.dto.UserDTO;
import com.spring.staymanager.entity.User;

public class UserMapper {

    public static UserDTO mapToUserDTO(User user){

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());
//        userDTO.setPassword(user.getPassword());

        if(user.getBookings() != null && !user.getBookings().isEmpty()){
            userDTO.setBookings(
                    user.getBookings().stream().map(BookingMapper::mapToBookingDTO).toList()
            );
        }

        return userDTO;

    }

    public static User mapToUser(UserDTO userDTO){
        User user = new User();

        if(userDTO.getId() != null){
            user.setId(userDTO.getId());
        }

        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole(userDTO.getRole());
        user.setPassword(userDTO.getPassword());

//        if((long) userDTO.getBookings().size() > 0){
//            user.setBookings(
//                    userDTO.getBookings().stream().map(BookingMapper::mapToBooking).toList()
//            );
//        }

        return user;
    }

    public static void updateUserFromDTO(UserDTO userDTO, User user){
        if(userDTO.getName() != null){
            user.setName(userDTO.getName());
        }
        if(userDTO.getEmail() != null){
            user.setEmail(userDTO.getEmail());
        }
        if(userDTO.getPhoneNumber() != null){
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if(userDTO.getPassword() != null){
            user.setPassword(userDTO.getPassword());
        }
//        if(userDTO.getBookings() != null){
//            user.setBookings(
//                    userDTO.getBookings().stream().map(BookingMapper::mapToBooking).toList()
//            );
//        }
        if(userDTO.getRole() != null){
            user.setRole(userDTO.getRole());
        }

    }

}
