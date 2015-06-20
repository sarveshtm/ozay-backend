package com.ozay.rowmapper;

import com.ozay.model.Permission;
import com.ozay.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class PermissionMapper implements RowMapper {

    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
        Permission permission = new Permission();
        permission.setName(rs.getString("name"));
        permission.setLabel(rs.getString("label"));
        permission.setType(rs.getLong("type"));
        return  permission;
    }
}
