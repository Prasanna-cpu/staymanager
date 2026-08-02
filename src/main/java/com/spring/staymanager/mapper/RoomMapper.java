package com.spring.staymanager.mapper;

import com.spring.staymanager.dto.RoomDTO;
import com.spring.staymanager.entity.Room;

public class RoomMapper {

    public static RoomDTO mapToRoomDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();

        roomDTO.setId(room.getId());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomDescription(room.getRoomDescription());

        if(room.getBookings() != null && !room.getBookings().isEmpty()){
            roomDTO.setBookings(
                    room.getBookings().stream().map(BookingMapper::mapToBookingDTO).toList()
            );
        }

        return roomDTO;

    }

    public static Room mapToRoom(RoomDTO roomDTO){
        Room room = new Room();

        if(roomDTO.getId() != null){
            room.setId(roomDTO.getId());
        }

        room.setRoomPrice(roomDTO.getRoomPrice());
        room.setRoomPhotoUrl(roomDTO.getRoomPhotoUrl());
        room.setRoomType(roomDTO.getRoomType());
        room.setRoomDescription(roomDTO.getRoomDescription());

        return room;
    }

}
