package com.ozay.repository;

import com.ozay.model.OrganizationAccess;
import com.ozay.model.RoleAccess;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;


@Repository
public class OrganizationAccessRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void create(OrganizationAccess organizationAccess){
        String query="INSERT INTO organization_access (user_id, organization_id, name) VALUES(:userId,:organizationId :name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationAccess.getUserId());
        params.addValue("organizationId", organizationAccess.getOrganizationId());
        params.addValue("name", organizationAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(OrganizationAccess organizationAccess){
        String query="UPDATE organization_access SET user_id=:userId,organization_id =:organizationId, name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationAccess.getUserId());
        params.addValue("organizationId", organizationAccess.getOrganizationId());
        params.addValue("name", organizationAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(OrganizationAccess organizationAccess){
        String query="DELETE FROM organization_access WHERE user_id=:userId AND organization_id =:organizationId AND name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationAccess.getUserId());
        params.addValue("organizationId", organizationAccess.getOrganizationId());
        params.addValue("name", organizationAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

}
