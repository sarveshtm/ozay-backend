package com.ozay.repository;

import com.ozay.model.Building;
import com.ozay.model.InvitedUser;
import com.ozay.rowmapper.BuildingRowMapper;
import com.ozay.rowmapper.InvitedUserRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public class InvitedUserRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public InvitedUser getOne(String activationKey){
        String query = "SELECT * FROM invited_user WHERE activation_key = :activation_key and activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activation_key", activationKey);

        return (InvitedUser)namedParameterJdbcTemplate.queryForObject(query, params, new InvitedUserRowMapper());
    }

    public void create(InvitedUser invitedUser){
        String query = "INSERT INTO invited_user" +
            "(user_detail_id, lang_key, activation_key, created_by) "
            + "VALUES(:user_detail_id, :lang_key, :activation_key, :created_by)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_detail_id", invitedUser.getMemberId());
        params.addValue("lang_key", invitedUser.getLangKey());
        params.addValue("activation_key", invitedUser.getActivationKey());
        params.addValue("created_by", invitedUser.getCreatedBy());

        namedParameterJdbcTemplate.update(query, params);
    }

    public void activateInvitedUser(InvitedUser invitedUser){
        String query = "UPDATE invited_user " +
            "SET activated =:activated, " +
            "activation_key = :activation_key, " +
            "activated_date = :activated_date " +
            "WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", invitedUser.getId());

        params.addValue("activated", invitedUser.isActivated());
        params.addValue("activated_date",  new Timestamp(invitedUser.getActivatedDate().getMillis()));
        params.addValue("activation_key", invitedUser.getActivationKey());

        namedParameterJdbcTemplate.update(query, params);
    }

}
