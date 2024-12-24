package com.project.LMS.service;

import com.project.LMS.dao.Users.MemberDao;
import com.project.LMS.dto.authentication.AuthenticationRequest;
import com.project.LMS.dto.authentication.AuthenticationResponse;
import com.project.LMS.dto.authentication.RegisterRequest;
import com.project.LMS.dao.Users.AdminDao;
import com.project.LMS.dao.Users.InstructorDao;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.entity.users.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final AdminDao adminDao;
    private final StudentDao studentDao;
    private final InstructorDao instructorDao;
    private final MemberDao memberDao;


    public AuthenticationResponse register(RegisterRequest request){
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        switch (request.getRole()){
            case STUDENT:
                studentDao.addStudent(user);
                break;
            case ADMIN:
                adminDao.addAdmin(user);
                break;
            case INSTRUCTOR:
                instructorDao.addInstructor(user);
                break;
        }

        memberDao.addMember(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = null;

        switch (request.getRole()) {
            case STUDENT:
                user = studentDao.getByEmail(request.getEmail());
                break;
            case ADMIN:
                user = adminDao.getByEmail(request.getEmail());
                break;
            case INSTRUCTOR:
                user = instructorDao.getByEmail(request.getEmail());
                break;
        }

        if (user == null) {
            throw new RuntimeException("User does not exist!");
        }

        if (!passwordMatches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect password!");
        }

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private boolean passwordMatches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
