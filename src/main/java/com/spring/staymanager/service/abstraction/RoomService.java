package com.spring.staymanager.service.abstraction;

import com.spring.staymanager.dto.RoomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    RoomDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description);

    List<RoomDTO> getAllRooms();

    List<String> getAllRoomTypes();

    void deleteRoom(String roomId);

    RoomDTO getRoomById(String roomId);

    RoomDTO updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo);

    List<RoomDTO> getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);

    List<RoomDTO> getAllAvailableRooms();

}
