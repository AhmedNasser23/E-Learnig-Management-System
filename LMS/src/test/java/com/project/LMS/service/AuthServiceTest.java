package com.project.LMS.service;

import com.project.LMS.dao.Users.AdminDao;
import com.project.LMS.dao.Users.InstructorDao;
import com.project.LMS.dao.Users.MemberDao;
import com.project.LMS.dao.Users.StudentDao;
import com.project.LMS.dto.authentication.AuthenticationRequest;
import com.project.LMS.dto.authentication.AuthenticationResponse;
import com.project.LMS.dto.authentication.RegisterRequest;
import com.project.LMS.entity.users.Student;
import com.project.LMS.entity.users.userRole;
import com.project.LMS.entity.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminDao adminDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private InstructorDao instructorDao;

    @Mock
    private MemberDao memberDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Student() {
        RegisterRequest request = new RegisterRequest("John", "john@example.com", "password123", userRole.STUDENT);
        User user = User.builder()
                .name("John")
                .email("john@example.com")
                .password("hashedPassword")
                .role(userRole.STUDENT)
                .build();

        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authService.register(request);

        verify(passwordEncoder, times(1)).encode("password123");
        verify(studentDao, times(1)).addStudent(refEq(user));
        verify(memberDao, times(1)).addMember(refEq(user));
        assertEquals("jwtToken", response.getToken());
    }


    @Test
    public void testRegister_Admin() {
        RegisterRequest request = new RegisterRequest("Admin", "admin@example.com", "password123", userRole.ADMIN);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authService.register(request);

        verify(adminDao, times(1)).addAdmin(any(User.class));
        verify(memberDao, times(1)).addMember(any(User.class));
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testRegister_Instructor() {
        RegisterRequest request = new RegisterRequest("Instructor", "instructor@example.com", "password123", userRole.INSTRUCTOR);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        AuthenticationResponse response = authService.register(request);

        verify(instructorDao, times(1)).addInstructor(any(User.class));
        verify(memberDao, times(1)).addMember(any(User.class));
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    public void testAuthenticate_Student() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "password123", userRole.STUDENT);
        Student student = new Student();
        student.setEmail("john@example.com");
        student.setPassword("hashedPassword"); // Simulated hashed password
        student.setRole(userRole.STUDENT);

        when(studentDao.getByEmail("john@example.com")).thenReturn(student);
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true); // Mock password match
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authService.authenticate(request);

        // Assert
        assertEquals("jwtToken", response.getToken());
        verify(studentDao, times(1)).getByEmail("john@example.com");
        verify(passwordEncoder, times(1)).matches("password123", "hashedPassword");
        verify(jwtService, times(1)).generateToken(any(User.class));
    }



    @Test
    public void testAuthenticate_WrongPassword() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "wrongPassword", userRole.STUDENT);
        Student student = new Student();
        student.setEmail("john@example.com");
        student.setPassword("hashedPassword");
        student.setRole(userRole.STUDENT);
        when(studentDao.getByEmail(anyString())).thenReturn(student);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.authenticate(request));
        assertEquals("Incorrect password!", exception.getMessage());
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("unknown@example.com", "password123", userRole.STUDENT);
        when(studentDao.getByEmail(anyString())).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.authenticate(request));
        assertEquals("User does not exist!", exception.getMessage());
    }
}
