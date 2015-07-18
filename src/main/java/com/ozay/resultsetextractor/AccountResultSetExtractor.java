package com.ozay.resultsetextractor;

import com.ozay.domain.Authority;
import com.ozay.model.AccountInformation;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class AccountResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<AccountInformation> list = new ArrayList<AccountInformation>();
        AccountInformation accountInformation = null;
        Set<String>authorities = new HashSet<String>();
        while(rs.next()){
            if(accountInformation == null){
                accountInformation = new AccountInformation();
                accountInformation.setSubscriberId(rs.getLong("s_user_id"));
                accountInformation.setOrganizationId(rs.getLong("organization_id"));
                accountInformation.setSubscriptionId(rs.getLong("s_id"));
            }
            authorities.add(rs.getString("name"));

        }
        if(accountInformation != null){
            System.out.println(123);
            if(authorities.size() >0){
                accountInformation.setAuthorities(authorities);
            }
            list.add(accountInformation);
        }

        return list;
    }
}
