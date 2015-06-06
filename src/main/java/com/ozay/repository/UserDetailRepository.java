package com.ozay.repository;

import com.ozay.model.UserDetail;
import com.ozay.rowmapper.UserDetailRowMapper;
import com.ozay.web.rest.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
public class UserDetailRepository {
    private final Logger log = LoggerFactory.getLogger(UserDetail.class);
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<UserDetail> getAllUsersByBuilding(int buildingId){
        return jdbcTemplate.query("Select * FROM user_detail WHERE building_id =?",

            new Object[]{buildingId}, new UserDetailRowMapper() {

            });
    }

    public Integer countActiveUnits(int buildingId){
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT unit) FROM USER_DETAIL WHERE building_id = ?", Integer.class, new Object[]{buildingId});
    }

    public List<UserDetail> getUserByBuildingEmailUnit(int buildingId, String email, String unit){
        return jdbcTemplate.query("Select * FROM user_detail WHERE building_id = ? AND email = ? AND UPPER(unit) = ?",
            new Object[]{buildingId, email, unit}, new UserDetailRowMapper() {
            });
    }



    public List<UserDetail> getUserEmailsForNotification(NotificationDTO notificationDTO){

        String query = "Select * FROM user_detail WHERE building_id = :buildingId ";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        parameterSource.addValue("buildingId", notificationDTO.getBuildingId());
        List<String> whereClauses = new ArrayList<String>();

        if(notificationDTO.isManagement() == true){
            whereClauses.add(" management = :management ");
            parameterSource.addValue("management", notificationDTO.isManagement());
        }
        if(notificationDTO.isStaff() == true){
            whereClauses.add(" staff = :staff ");
            parameterSource.addValue("staff", notificationDTO.isStaff());

        }
        if(notificationDTO.isBoard() == true){
            whereClauses.add(" board = :board ");

            parameterSource.addValue("board", notificationDTO.isBoard());
        }
        if(notificationDTO.isResident() == true){
            whereClauses.add(" resident = :resident ");
            parameterSource.addValue("resident", notificationDTO.isResident());
        }


        boolean hasIds = false;
        if(notificationDTO.isIndividual() == true && notificationDTO.getIndividuals().size() > 0){
            log.debug("Notification individual id : {}", notificationDTO.getIndividuals());
            hasIds = true;
            parameterSource.addValue("ids", notificationDTO.getIndividuals());
        }
        String restQuery = "";
        if(whereClauses.size() > 0 || (notificationDTO.isIndividual() == true && notificationDTO.getIndividuals().size() > 0)){
            boolean hasAndQuery = false;
            restQuery += " AND ( ";
            for(int i = 0; i<whereClauses.size(); i++){
                hasAndQuery = true;
                if(i != 0){
                    restQuery += " AND ";
                }
                restQuery += whereClauses.get(i);
            }
            if(hasIds == true){
                if(hasAndQuery == true){
                    restQuery += " OR ";
                }
                restQuery += " id IN ( :ids ) ";
            }

            restQuery += " ) ";
        }
        log.debug("Notification individual query : {}", query + restQuery);



        return namedParameterJdbcTemplate.query(query + restQuery,
            parameterSource ,  new UserDetailRowMapper() );
    }


    public UserDetail getUserByBuilding(int user_id, int buildingId){
        return (UserDetail)jdbcTemplate.queryForObject("Select * FROM user_detail WHERE building_id = ? AND id = ?",
            new Object[]{buildingId, user_id}, new UserDetailRowMapper(){
            });
    }

    public UserDetail getUserDetailByBuildingAndUserId(int id, int buildingId){
        return (UserDetail)jdbcTemplate.queryForObject("Select * FROM user_detail WHERE building_id = ? AND user_id = ?",
            new Object[]{buildingId, id}, new UserDetailRowMapper(){
            });
    }



    public UserDetail getUserByBuilding(String login, int buildingId){
        return (UserDetail)jdbcTemplate.queryForObject("Select * FROM user_detail WHERE building_id = ? AND login = ?",
            new Object[]{buildingId, login}, new UserDetailRowMapper(){
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
            "user_id, " +
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
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = new Object[] { userDetail.getLogin(),
            userDetail.getUserId(),
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
