package com.ozay.resultsetextractor;

import com.ozay.model.Member;
import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class MemberResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<Member> list = new ArrayList<Member>();
        Member member = null;
        Long previous = null;
        while(rs.next()){
            if(previous == null || previous != rs.getLong("id")){
                if(previous != null){
                    list.add(member);
                }
                previous = rs.getLong("id");
                member = new Member();
                member.setId(rs.getLong("id"));
                member.setLogin(rs.getString("login"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setBuildingId(rs.getInt("building_id"));
                member.setOwnership(rs.getDouble("ownership"));
                member.setUnit(rs.getString("unit"));
                member.setParking(rs.getString("parking"));
                member.setDeleted(rs.getBoolean("deleted"));
                member.setUserId(rs.getLong("user_id"));
                member.setRoles(new HashSet<Role>());
            }
            Role role = new Role();
            role.setId(rs.getLong("r_id"));
            role.setBuildingId(rs.getLong("r_building_id"));
            role.setName(rs.getString("r_name"));
            role.setSortOrder(rs.getLong("r_sort_order"));
            role.setOrganizationUserRole(rs.getBoolean("r_organization_user_role"));
            role.setBelongTo(rs.getLong("r_belong_to"));
            if(role.getId() != 0){

                member.getRoles().add(role);
            }
        }
        if(member != null){
            list.add(member);
        }
        return list;
    }
}
