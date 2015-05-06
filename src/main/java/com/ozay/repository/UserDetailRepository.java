package com.ozay.repository;

import com.ozay.model.Building;
import com.ozay.model.UserDetail;
import com.ozay.rowmapper.BuildingRowMapper;
import com.ozay.rowmapper.UserDetailRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class UserDetailRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;

    public List<UserDetail> getAllUsersByBuilding(int buildingId){
        return jdbcTemplate.query("Select * FROM user_building ub INNER JOIN t_user u ON ub.login = u.login INNER JOIN user_detail ud ON ud.login = u.login WHERE ub.building_id =? ORDER BY u.login",

            new Object[]{buildingId}, new UserDetailRowMapper(){

        });
    }

    public UserDetail getAllUserByBuilding(String username, int buildingId){
        return (UserDetail)jdbcTemplate.queryForObject("Select * FROM user_building ub INNER JOIN t_user u ON ub.login = u.login INNER JOIN user_detail ud ON ud.login = u.login WHERE ub.building_id = ? AND u.login = ?",
            new Object[]{buildingId, username}, new UserDetailRowMapper(){
            });
    }

    public boolean create(UserDetail userDetail){
        String insert = "INSERT INTO user_detail(" +
            "login, " +
            "building_id, " +
            "ownership, " +
            "renter, " +
            "unit, " +
            "expiration_date, " +
            "parking, " +
            "management, " +
            "staff, " +
            "board, " +
            "resident ) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[] { userDetail.getLogin(),
            userDetail.getBuildingId(),
            userDetail.getOwnership(),
            userDetail.getRenter(),
            userDetail.getUnit(),
            userDetail.getExpirationDate(),
            userDetail.getParking(),
            userDetail.getManagement(),
            userDetail.getStaff(),
            userDetail.getBoard(),
            userDetail.getResident() };

        int count = jdbcTemplate.update(insert, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }

    }
    public boolean update(UserDetail userDetail){
        String update = "UPDATE user_detail SET " +
            "ownership = ?, " +
            "renter = ?, " +
            "unit = ?, " +
            "expiration_date = ?, " +
            "parking = ?, " +
            "management = ?, " +
            "staff = ?, " +
            "board = ?, " +
            "resident = ? " +
            "WHERE building_id = ? " +
            "AND login = ?";

        Object[] params = new Object[] {
            userDetail.getOwnership(),
            userDetail.getRenter(),
            userDetail.getUnit(),
            userDetail.getExpirationDate(),
            userDetail.getParking(),
            userDetail.getManagement(),
            userDetail.getStaff(),
            userDetail.getBoard(),
            userDetail.getResident(),
            userDetail.getBuildingId(),
            userDetail.getLogin()
        };

        int count = jdbcTemplate.update(update, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

}
