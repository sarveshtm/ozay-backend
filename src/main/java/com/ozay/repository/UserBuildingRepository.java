package com.ozay.repository;

import com.ozay.model.UserDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;


@Repository
public class UserBuildingRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;


    public boolean create(UserDetail userDetail){
        String insert = "INSERT INTO user_building(" +
            "user_id, " +
            "building_id )" +
            "VALUES(?, ?)";
        Object[] params = new Object[] {userDetail.getUserId(),
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
