package com.spring.staymanager.service.implementation;

import com.spring.staymanager.dto.RoomDTO;
import com.spring.staymanager.entity.Room;
import com.spring.staymanager.exception.ObjectNotFoundException;
import com.spring.staymanager.mapper.RoomMapper;
import com.spring.staymanager.repository.RoomRepository;
import com.spring.staymanager.service.abstraction.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, RuntimeException.class, ObjectNotFoundException.class})
@Slf4j
public class RoomServiceImplementation implements RoomService {

    private final RoomRepository roomRepository;
    private final AWSS3Service awss3Service;

    @Override
    public RoomDTO addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {
        Room room = new Room();

        room.setRoomPhotoUrl(awss3Service.saveImageToRemoteBucket(photo));
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        room.setRoomDescription(description);

        Room savedRoom = roomRepository.save(room);
        RoomDTO savedRoomDTO = RoomMapper.mapToRoomDTO(savedRoom);
        return savedRoomDTO;
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomDTO> roomDTOS = rooms.stream().map(RoomMapper::mapToRoomDTO).toList();
        return roomDTOS;
    }

    @Override
    public List<String> getAllRoomTypes() {
        List<String> roomTypes = roomRepository.findDistinctRoomTypes();
        return roomTypes;
    }

    @Override
    public void deleteRoom(String roomId) {
        roomRepository.findById(roomId)
                .ifPresentOrElse(roomRepository::delete,
                        () -> {
                            throw new ObjectNotFoundException("Room not found");
                        });
    }

    @Override
    public RoomDTO getRoomById(String roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ObjectNotFoundException("Room not found"));
        RoomDTO roomDTO = RoomMapper.mapToRoomDTO(room);
        return roomDTO;
    }

    @Override
    public RoomDTO updateRoom(String roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        String imageUrl = "";

        if(photo != null && !photo.isEmpty()){
            imageUrl = awss3Service.saveImageToRemoteBucket(photo);
        }

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new ObjectNotFoundException("Room not found"));
        if (roomType != null) room.setRoomType(roomType);
        if (roomPrice != null) room.setRoomPrice(roomPrice);
        if (description != null) room.setRoomDescription(description);
        if (imageUrl != null && !imageUrl.isEmpty()) room.setRoomPhotoUrl(imageUrl);

        Room updatedRoom = roomRepository.save(room);
        RoomDTO updatedRoomDTO = RoomMapper.mapToRoomDTO(updatedRoom);
        return updatedRoomDTO;

    }

    @Override
    public List<RoomDTO> getAvailableRoomsByDataAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        List<Room> rooms = roomRepository.findAvailableRoomsByDateAndTypes(checkInDate, checkOutDate, roomType);
        List<RoomDTO> roomDTOS = rooms.stream().map(RoomMapper::mapToRoomDTO).toList();
        return roomDTOS;
    }

    @Override
    public List<RoomDTO> getAllAvailableRooms() {
        List<Room> rooms = roomRepository.getAllAvailableRooms();
        List<RoomDTO> roomDTOS = rooms.stream().map(RoomMapper::mapToRoomDTO).toList();
        return roomDTOS;
    }
}
