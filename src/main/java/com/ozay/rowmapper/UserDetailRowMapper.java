package com.ozay.rowmapper;

import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.model.UserDetail;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class UserDetailRowMapper implements RowMapper {

    public UserDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDetail userDetail = new UserDetail();
        userDetail.setId(rs.getInt("id"));
        userDetail.setLogin(rs.getString("login"));
        userDetail.setEmail(rs.getString("email"));
        userDetail.setPhone(rs.getString("phone"));
        userDetail.setFirstName(rs.getString("first_name"));
        userDetail.setLastName(rs.getString("last_name"));
        userDetail.setBuildingId(rs.getInt("building_id"));
        userDetail.setOwnership(rs.getDouble("ownership"));
        userDetail.setRenter(rs.getBoolean("renter"));
        userDetail.setUnit(rs.getString("unit"));
        userDetail.setParking(rs.getString("parking"));
        userDetail.setManagement(rs.getBoolean("management"));
        userDetail.setStaff(rs.getBoolean("staff"));
        userDetail.setBoard(rs.getBoolean("board"));
        userDetail.setResident(rs.getBoolean("resident"));

        return userDetail;
    }
}
