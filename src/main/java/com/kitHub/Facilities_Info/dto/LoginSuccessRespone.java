package com.kitHub.Facilities_info.dto;

import com.kitHub.Facilities_info.domain.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginSuccessRespone {
    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;

}
