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

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class MemberResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<Member> list = new ArrayList<Member>();
        Member member = null;
        while(rs.next()){
            if(member == null){
                member = new Member();
                member.setId(rs.getInt("id"));
                member.setLogin(rs.getString("login"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setBuildingId(rs.getInt("building_id"));
                member.setOwnership(rs.getDouble("ownership"));
                member.setRenter(rs.getBoolean("renter"));
                member.setUnit(rs.getString("unit"));
                member.setParking(rs.getString("parking"));
                member.setManagement(rs.getBoolean("management"));
                member.setStaff(rs.getBoolean("staff"));
                member.setBoard(rs.getBoolean("board"));
                member.setResident(rs.getBoolean("resident"));
                member.setDeleted(rs.getBoolean("deleted"));
                member.setUserId(rs.getLong("user_id"));
            }
            Role role = new Role();
            role.setBuildingId(rs.getLong("building_id"));
            role.setName(rs.getString("name"));
            role.setSortOrder(rs.getLong("sort_order"));
            role.setOrganizationUserRole(rs.getBoolean("organization_user_role"));
            role.setBelongTo(rs.getLong("belong_to"));
            member.getRoles().add(role);

        }
        list.add(member);

        return list;
    }
}
