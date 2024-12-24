package com.project.LMS.dao.Users;

import com.project.LMS.entity.users.Member;
import com.project.LMS.entity.users.User;
import com.project.LMS.entity.users.userRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_MEMBER_BY_EMAIL = "SELECT * FROM Members WHERE email = ?";
    private static final String INSERT_MEMBER_SQL = "INSERT INTO Members (name, password, email, profile_info, role) VALUES (?, ?, ?, ?, ?)";

    // RowMapper for Member
    private final RowMapper<Member> memberRowMapper = (rs, rowNum) -> {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setName(rs.getString("name"));
        member.setPassword(rs.getString("password"));
        member.setEmail(rs.getString("email"));
        member.setProfileInfo(rs.getString("profile_info"));
        member.setRole(userRole.valueOf(rs.getString("role"))); // Ensure this matches enum values
        return member;
    };

    // Get member by email
    public Member getByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_MEMBER_BY_EMAIL, new Object[]{email}, memberRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null; // Or throw an exception like UsernameNotFoundException
        }
    }

    // Add new member
    public void addMember(User member) {
        jdbcTemplate.update(
                INSERT_MEMBER_SQL,
                member.getName(),
                member.getPassword(), // Ensure password is hashed
                member.getEmail(),
                member.getProfileInfo(),
                member.getRole().toString()
        );
    }
}
