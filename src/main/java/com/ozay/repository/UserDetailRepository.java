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


}
