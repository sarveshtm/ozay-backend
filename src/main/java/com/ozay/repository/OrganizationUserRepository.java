package com.ozay.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;


@Repository
public class OrganizationUserRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void create(long userId, long organizationId){
        String query="INSERT INTO organization_user (user_id, organization_id) VALUES(:userId, :organizationId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("organizationId", organizationId);
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(long userId, long organizationId){
        String query="DELETE FROM organization_user WHERE user_id:userId AND organization_id=:organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("organizationId", organizationId);
        namedParameterJdbcTemplate.update(query,params);
    }
}
