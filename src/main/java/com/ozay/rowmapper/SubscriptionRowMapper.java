package com.ozay.rowmapper;

import com.ozay.model.Building;
import com.ozay.model.Subscription;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SubscriptionRowMapper implements RowMapper {

    public Subscription mapRow(ResultSet rs, int rowNum) throws SQLException {
        Subscription subscription = new Subscription();
        subscription.setId(rs.getLong("id"));
        subscription.setUserId(rs.getLong("user_id"));
        subscription.setCreatedDate(new DateTime(rs.getDate("created_date")));
        subscription.setDateFrom(new DateTime(rs.getDate("date_from")));
        subscription.setDateTo(new DateTime(rs.getDate("date_to")));
        return subscription;
    }
}
