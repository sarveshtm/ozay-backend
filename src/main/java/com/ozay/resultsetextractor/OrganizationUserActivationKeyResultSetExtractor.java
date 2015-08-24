package com.ozay.resultsetextractor;

import com.ozay.model.OrganizationUserActivationKey;
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
public class OrganizationUserActivationKeyResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<OrganizationUserActivationKey> list = null;
        OrganizationUserActivationKey organizationUserActivationKey = null;
        while(rs.next()){
            organizationUserActivationKey = new OrganizationUserActivationKey();
            organizationUserActivationKey.setId(rs.getLong("id"));
            organizationUserActivationKey.setUserId(rs.getLong("user_id"));
            organizationUserActivationKey.setActivationKey(rs.getString("activation_key"));
            if(list == null){
                list = new ArrayList<OrganizationUserActivationKey>();
            }
            list.add(organizationUserActivationKey);
            break;
        }


        return list;
    }
}
