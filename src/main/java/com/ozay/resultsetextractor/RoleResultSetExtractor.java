package com.ozay.resultsetextractor;

import com.ozay.domain.Authority;
import com.ozay.model.AccountInformation;
import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class RoleResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<Role> list = new ArrayList<Role>();
        Role role = null;
        while(rs.next()){
            if(role == null){
                role = new Role();
                role.setRolePermissions(new HashSet<RolePermission>());
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                role.setSortOrder(rs.getLong("sort_order"));
                role.setOrganizationUserRole(rs.getBoolean("organization_user_role"));
                role.setBelongTo(rs.getLong("belong_to"));
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(rs.getLong("id"));
            rolePermission.setName(rs.getString("rp_name"));
            if(rolePermission.getName() != null){
                role.getRolePermissions().add(rolePermission);
            }
        }
        if(role != null){
            list.add(role);
        }

        return list;
    }
}
