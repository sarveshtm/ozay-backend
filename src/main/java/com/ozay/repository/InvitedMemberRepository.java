package com.ozay.repository;

import com.ozay.model.InvitedMember;
import com.ozay.rowmapper.InvitedMemberRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;


@Repository
public class InvitedMemberRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public InvitedMember getOne(String activationKey){
        String query = "SELECT * FROM invited_member WHERE activation_key = :activation_key and activated = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activation_key", activationKey);

        return (InvitedMember)namedParameterJdbcTemplate.queryForObject(query, params, new InvitedMemberRowMapper());
    }

    public void create(InvitedMember invitedMember){
        String query = "INSERT INTO invited_member" +
            "(member_id, lang_key, activation_key, created_by) "
            + "VALUES(:member_id, :lang_key, :activation_key, :created_by)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("member_id", invitedMember.getMemberId());
        params.addValue("lang_key", invitedMember.getLangKey());
        params.addValue("activation_key", invitedMember.getActivationKey());
        params.addValue("created_by", invitedMember.getCreatedBy());

        namedParameterJdbcTemplate.update(query, params);
    }

    public void activateInvitedMember(InvitedMember invitedMember){
        String query = "UPDATE invited_member " +
            "SET activated =:activated, " +
            "activation_key = :activation_key, " +
            "activated_date = :activated_date " +
            "WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", invitedMember.getId());

        params.addValue("activated", invitedMember.isActivated());
        params.addValue("activated_date",  new Timestamp(invitedMember.getActivatedDate().getMillis()));
        params.addValue("activation_key", invitedMember.getActivationKey());

        namedParameterJdbcTemplate.update(query, params);
    }

}
