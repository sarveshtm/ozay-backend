package com.ozay.repository;

import com.ozay.model.OrganizationPermission;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;


@Repository
public class OrganizationAccessRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void create(OrganizationPermission organizationPermission){
        String query="INSERT INTO organization_access (user_id, organization_id, name) VALUES(:userId,:organizationId :name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationPermission.getUserId());
        params.addValue("organizationId", organizationPermission.getOrganizationId());
        params.addValue("name", organizationPermission.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(OrganizationPermission organizationPermission){
        String query="UPDATE organization_access SET user_id=:userId,organization_id =:organizationId, name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationPermission.getUserId());
        params.addValue("organizationId", organizationPermission.getOrganizationId());
        params.addValue("name", organizationPermission.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(OrganizationPermission organizationPermission){
        String query="DELETE FROM organization_access WHERE user_id=:userId AND organization_id =:organizationId AND name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationPermission.getUserId());
        params.addValue("organizationId", organizationPermission.getOrganizationId());
        params.addValue("name", organizationPermission.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

}
