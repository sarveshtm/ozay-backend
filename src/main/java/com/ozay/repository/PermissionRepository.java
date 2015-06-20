package com.ozay.repository;

import com.ozay.domain.User;
import com.ozay.model.Organization;
import com.ozay.model.Permission;
import com.ozay.rowmapper.OrganizationMapper;
import com.ozay.rowmapper.PermissionMapper;
import com.ozay.rowmapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class PermissionRepository {


    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Permission> getOrganizationPermissions(){
        String query = "SELECT * FROM permission where type=1";
        return namedParameterJdbcTemplate.query(query, new PermissionMapper());
    }

    public List<Permission> getRolePermissions(){
        String query = "SELECT * FROM permission where type=2";
        return namedParameterJdbcTemplate.query(query, new PermissionMapper());
    }

}
