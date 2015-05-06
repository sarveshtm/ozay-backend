package com.ozay.repository;

import com.ozay.model.UserDetail;
import com.ozay.rowmapper.UserDetailRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class UserBuildingRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;


    public boolean create(UserDetail userDetail){
        String insert = "INSERT INTO user_building(" +
            "login, " +
            "building_id )" +
            "VALUES(?, ?)";
        Object[] params = new Object[] { userDetail.getLogin(),
            userDetail.getBuildingId()
             };

        int count = jdbcTemplate.update(insert, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }


    }

}
