package com.project.LMS.dto.authentication;


import com.project.LMS.entity.users.userRole;
import lombok.*;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private userRole role;
}
