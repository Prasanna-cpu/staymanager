package com.spring.staymanager.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.staymanager.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    public String id;

    public String email;

    public String name;

    public String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;

    public Role role;

    public List<BookingDTO> bookings;


}
