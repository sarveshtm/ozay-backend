package com.ozay.rowmapper;

import com.ozay.model.Member;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class MemberRowMapper implements RowMapper {

    public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setLogin(rs.getString("login"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setBuildingId(rs.getInt("building_id"));
        member.setOwnership(rs.getDouble("ownership"));
        member.setRenter(rs.getBoolean("renter"));
        member.setUnit(rs.getString("unit"));
        member.setParking(rs.getString("parking"));
        member.setManagement(rs.getBoolean("management"));
        member.setStaff(rs.getBoolean("staff"));
        member.setBoard(rs.getBoolean("board"));
        member.setResident(rs.getBoolean("resident"));
        member.setDeleted(rs.getBoolean("deleted"));
        member.setUserId(rs.getLong("user_id"));

        return member;
    }
}
