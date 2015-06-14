package com.ozay.repository;

import com.ozay.model.Role;
import com.ozay.model.RoleAccess;
import com.ozay.rowmapper.RoleMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class RoleAccessRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public void create(RoleAccess roleAccess){
        String query="INSERT INTO role_access (role_id, name) VALUES(:roleId, :name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleAccess.getRoleId());
        params.addValue("name", roleAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(RoleAccess roleAccess){
        String query="UPDATE role_access SET role_id=:roleId, name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleAccess.getRoleId());
        params.addValue("name", roleAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(RoleAccess roleAccess){
        String query="DELETE FROM role_access WHERE role_id=:roleId AND name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleAccess.getRoleId());
        params.addValue("name", roleAccess.getName());
        namedParameterJdbcTemplate.update(query,params);
    }


}
