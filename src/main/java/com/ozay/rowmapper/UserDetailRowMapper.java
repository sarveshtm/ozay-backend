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
        userDetail.setLogin(rs.getString("login"));
        userDetail.setBuildingId(rs.getInt("building_id"));
        userDetail.setAddress_1(rs.getString("address_1"));
        userDetail.setAddress_2(rs.getString("address_2"));
        userDetail.setState(rs.getString("state"));
        userDetail.setZip(rs.getString("zip"));
        userDetail.setOwnership(rs.getDouble("ownership"));
        userDetail.setRenter(rs.getBoolean("renter"));
        userDetail.setUnit(rs.getString("unit"));
        userDetail.setParking(rs.getString("parking"));
        userDetail.setManagement(rs.getBoolean("management"));
        userDetail.setStaff(rs.getBoolean("staff"));
        userDetail.setBoard(rs.getBoolean("board"));
        userDetail.setResident(rs.getBoolean("resident"));

        User user = new User();
        user.setPhone(rs.getString("phone"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));


        userDetail.setUser(user);
        return userDetail;
    }
}
