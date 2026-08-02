package com.spring.staymanager.controller;


import com.spring.staymanager.dto.RoomDTO;
import com.spring.staymanager.entity.Room;
import com.spring.staymanager.response.ApiResponse;
import com.spring.staymanager.service.abstraction.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/add-room")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> addNewRoomHandler(
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription
    ){
        RoomDTO addedRoom = roomService.addNewRoom(photo, roomType, roomPrice, roomDescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse(
                        addedRoom,
                        "Room Added",
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED
                )
        );
    }

    @GetMapping("/all-rooms")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllRoomsHandler(){
        List<RoomDTO> roomDTOS = roomService.getAllRooms();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        roomDTOS,
                        "Rooms Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/room-types")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllRoomTypesHandler(){
        List<String> types = roomService.getAllRoomTypes();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        types,
                        "Room Types Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/room/{roomId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<ApiResponse> getRoomByIdHandler(
            @PathVariable String roomId
    ){
        RoomDTO roomDTO = roomService.getRoomById(roomId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        roomDTO,
                        "Room Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/available-rooms-by-date-and-type")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllAvailableRoomsByDataAndType(
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate checkOutDate,
            @RequestParam(required = false) String roomType
    ){
        List<RoomDTO> roomDTOS = roomService.getAvailableRoomsByDataAndType(checkInDate, checkOutDate, roomType);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        roomDTOS,
                        "Available Rooms Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @GetMapping("/available-rooms")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN, ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllAvailableRooms(){
        List<RoomDTO> roomDTOS = roomService.getAllAvailableRooms();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        roomDTOS,
                        "Available Rooms Retrieved",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }

    @PutMapping("/update/{roomId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateRoom(
            @PathVariable String roomId,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam(value = "roomType", required = false) String roomType,
            @RequestParam(value = "roomPrice", required = false) BigDecimal roomPrice,
            @RequestParam(value = "roomDescription", required = false) String roomDescription

    ) {
        RoomDTO roomDTO = roomService.updateRoom(roomId, roomDescription, roomType, roomPrice, photo);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse(
                        roomDTO,
                        "Room Updated",
                        HttpStatus.OK.value(),
                        HttpStatus.OK
                )
        );
    }


}
