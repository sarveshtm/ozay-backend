package com.ozay.resultsetextractor;

import com.ozay.web.rest.dto.OrganizationUserDTO;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryohei on 7/17/15.
 */
public class OrgRoleResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<OrganizationUserDTO> list = new ArrayList<OrganizationUserDTO>();
        List<String> roleList = new ArrayList<String>();
        OrganizationUserDTO user = null;
        while(rs.next()){
            if(user == null){
                user  = new OrganizationUserDTO();
                user.setUserId(rs.getLong("id"));
                user.setOrganizationId(rs.getLong("organization_id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setActivated(rs.getBoolean("activated"));
            }
            roleList.add(rs.getString("org_permission"));
        }
        if (roleList.size() > 0) user.setRoles(roleList);
        list.add(user);
        return list;
    }

//    public Object extractData(ResultSet rs) throws SQLException{
//        List<OrganizationUserDTO> list = new ArrayList<OrganizationUserDTO>();
//        OrganizationUserDTO role = null;
//        while(rs.next()){
//            if(role == null){
//                OrganizationUserDTO organizationUser  = new OrganizationUserDTO();
//                organizationUser.setOrganizationId(rs.getLong("sort_order"));
//                organizationUser.setOrganizationId(rs.getLong("sort_order"));
//                organizationUser.setOrganizationId(rs.getLong("sort_order"));
//                organizationUser.setOrganizationId(rs.getLong("sort_order"));
//                organizationUser.setOrganizationId(rs.getLong("sort_order"));
//            }
//            RolePermission rolePermission = new RolePermission();
//            rolePermission.setRoleId(rs.getLong("id"));
//            rolePermission.setName(rs.getString("rp_name"));
//            if(rolePermission.getName() != null){
//                organizationUser.getRolePermissions().add(rolePermission);
//            }
//        }
//        list.add(role);
//
//        return list;
//    }
}
