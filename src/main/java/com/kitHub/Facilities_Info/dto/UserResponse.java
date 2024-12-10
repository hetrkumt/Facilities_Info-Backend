package com.kitHub.Facilities_info.dto;

import com.kitHub.Facilities_info.domain.user.User;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class UserResponse {
    private User user;
    private String message;
}