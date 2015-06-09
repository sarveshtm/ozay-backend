package com.ozay.repository;

import com.ozay.model.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;


@Repository
public class UserBuildingRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;


    public boolean create(Member member){
        String insert = "INSERT INTO user_building(" +
            "user_id, " +
            "building_id )" +
            "VALUES(?, ?)";
        Object[] params = new Object[] {member.getUserId(),
            member.getBuildingId()
        };

        int count = jdbcTemplate.update(insert, params);
        if(count > 0){
            return true;
        } else {
            return false;
        }


    }

}
