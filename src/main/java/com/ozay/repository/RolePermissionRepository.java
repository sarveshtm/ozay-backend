package com.ozay.repository;

import com.ozay.model.RolePermission;
import com.ozay.rowmapper.RolePermissionMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class RolePermissionRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RolePermission> findAllByRoleId(Long roleId){
        String query = "SELECT * FROM role_permission WHERE role_id = :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", roleId);
        return namedParameterJdbcTemplate.query(query, params, new RolePermissionMapper());
    }

    public void create(RolePermission rolePermission){
        String query="INSERT INTO role_permission (role_id, name) VALUES(:roleId, :name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", rolePermission.getRoleId());
        params.addValue("name", rolePermission.getName());
        namedParameterJdbcTemplate.update(query,params);
    }

//    public void update(RolePermission rolePermission){
//        String query="UPDATE role_permission SET role_id=:roleId, name=:name";
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("roleId", rolePermission.getRoleId());
//        params.addValue("name", rolePermission.getName());
//        namedParameterJdbcTemplate.update(query,params);
//    }

    public void delete(RolePermission rolePermission){
        String query="DELETE FROM role_permission WHERE role_id=:roleId AND name=:name";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("roleId", rolePermission.getRoleId());
        params.addValue("name", rolePermission.getName());
        namedParameterJdbcTemplate.update(query,params);
    }


}
