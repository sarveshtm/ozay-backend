package com.ozay.rowmapper;

import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class RolePermissionMapper implements RowMapper {

    public RolePermission mapRow(ResultSet rs, int rowNum) throws SQLException {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleId(rs.getLong("role_id"));
        rolePermission.setName(rs.getString("name"));
        return rolePermission;
    }
}
