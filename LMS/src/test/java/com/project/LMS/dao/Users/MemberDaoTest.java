package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Member;
import com.project.LMS.entity.users.User;
import com.project.LMS.entity.users.userRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddMember() {
        User member = new User();
        member.setName("John Doe");
        member.setPassword("hashedPassword123");
        member.setEmail("john.doe@example.com");
        member.setProfileInfo("Some profile info");
        member.setRole(userRole.MEMBER);

        memberDao.addMember(member);

        verify(jdbcTemplate).update(
                eq("INSERT INTO Members (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)"),
                eq("John Doe"),
                eq("hashedPassword123"),
                eq("john.doe@example.com"),
                eq("Some profile info"),
                eq("MEMBER")
        );
    }

    @Test
    void testGetByEmailWhenMemberExists() {
        String email = "john.doe@example.com";
        Member mockMember = new Member();
        mockMember.setId(1);
        mockMember.setName("John Doe");
        mockMember.setPassword("hashedPassword123");
        mockMember.setEmail(email);
        mockMember.setProfileInfo("Some profile info");
        mockMember.setRole(userRole.MEMBER);

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Members WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenReturn(mockMember);

        Member member = memberDao.getByEmail(email);

        assertNotNull(member);
        assertEquals(1, member.getId());
        assertEquals("John Doe", member.getName());
        assertEquals("hashedPassword123", member.getPassword());
        assertEquals(email, member.getEmail());
        assertEquals("Some profile info", member.getProfileInfo());
        assertEquals(userRole.MEMBER, member.getRole());
    }

    @Test
    void testGetByEmailWhenMemberDoesNotExist() {
        String email = "nonexistent@example.com";

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Members WHERE email = ?"),
                any(Object[].class),
                (RowMapper<Object>) any()
        )).thenThrow(new EmptyResultDataAccessException(1));

        Member member = memberDao.getByEmail(email);

        assertNull(member);
    }
}
