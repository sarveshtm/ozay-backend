package com.ozay.repository;

import com.ozay.model.Organization;
import com.ozay.model.Subscription;
import com.ozay.rowmapper.RoleMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;


@Repository
public class SubscriptionRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Subscription> findAllSubscriptionsByUser(long userId){
        String query = "SELECT * FROM subscriptions where user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        return namedParameterJdbcTemplate.query(query, params, new RoleMapper());
    }

    public void create(Subscription subscription){
        String query = "INSERT INTO subscription (user_id, created_date, date_from, date_to) " +
            "VALUES(:userId, now(), now(), now() + interval '1 year')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", subscription.getUserId());

        namedParameterJdbcTemplate.update(query,params);
    }

}
