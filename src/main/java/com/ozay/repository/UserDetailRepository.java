package com.ozay.repository;

import com.ozay.model.UserDetail;
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
        return jdbcTemplate.query("Select * FROM user_detail WHERE building_id =?",

            new Object[]{buildingId}, new UserDetailRowMapper() {

            });
    }

    public List<UserDetail> getUserByBuildingEmailUnit(int buildingId, String email, String unit){
        return jdbcTemplate.query("Select * FROM user_detail WHERE building_id = ? AND email = ? AND UPPER(unit) = ?",
            new Object[]{buildingId, email, unit}, new UserDetailRowMapper() {
            });
    }


    public UserDetail getUserByBuilding(int id, int buildingId){
        return (UserDetail)jdbcTemplate.queryForObject("Select * FROM user_detail WHERE building_id = ? AND id = ?",
            new Object[]{buildingId, id}, new UserDetailRowMapper(){
            });
    }

    public List<UserDetail> searchUsers(int buildingId, String item){
        String likeItem = "%" + item.toLowerCase() + "%";
        return jdbcTemplate.query("Select * FROM user_detail " +
                "WHERE building_id =? AND " +
                "(LOWER(first_name) LIKE ? " +
                "OR LOWER(last_name) LIKE ? " +
                "OR LOWER(phone) LIKE ? " +
                "OR LOWER(email) LIKE ? " +
                "OR LOWER(unit) LIKE ? " +
                "OR LOWER(parking) LIKE ?)",

            new Object[]{buildingId, item, likeItem, likeItem, likeItem, likeItem, likeItem}, new UserDetailRowMapper(){

            });
    }

    public boolean create(UserDetail userDetail){
        String insert = "INSERT INTO user_detail(" +
            "login, " +
            "first_name," +
            "last_name," +
            "email," +
            "phone," +
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
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[] { userDetail.getLogin(),
            userDetail.getFirstName(),
            userDetail.getLastName(),
            userDetail.getEmail(),
            userDetail.getPhone(),
            userDetail.getBuildingId(),
            userDetail.getOwnership(),
            userDetail.isRenter(),
            userDetail.getUnit(),
            userDetail.getExpirationDate(),
            userDetail.getParking(),
            userDetail.isManagement(),
            userDetail.isStaff(),
            userDetail.isBoard(),
            userDetail.isResident() };

        int count = jdbcTemplate.update(insert, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }

    }
    public boolean update(UserDetail userDetail){
        String update = "UPDATE user_detail SET " +
            "first_name = ?, " +
            "last_name = ?, " +
            "email = ?, " +
            "phone = ?, " +
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
            "AND id = ?";

        Object[] params = new Object[] {
            userDetail.getFirstName(),
            userDetail.getLastName(),
            userDetail.getEmail(),
            userDetail.getPhone(),
            userDetail.getOwnership(),
            userDetail.isRenter(),
            userDetail.getUnit(),
            userDetail.getExpirationDate(),
            userDetail.getParking(),
            userDetail.isManagement(),
            userDetail.isStaff(),
            userDetail.isBoard(),
            userDetail.isResident(),
            userDetail.getBuildingId(),
            userDetail.getId()

        };

        int count = jdbcTemplate.update(update, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

}
