package com.ozay.resultsetextractor;

import com.ozay.domain.Authority;
import com.ozay.model.AccountInformation;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class AccountResultSetExtractor implements ResultSetExtractor {

    public Object extractData(ResultSet rs) throws SQLException{
        List<AccountInformation> list = new ArrayList<AccountInformation>();
        AccountInformation accountInformation = null;
        HashMap<String,Authority> map = new HashMap<String,Authority>();
        while(rs.next()){
            if(accountInformation == null){
                accountInformation = new AccountInformation();
                accountInformation.setSubscriberId(rs.getLong("s_user_id"));
                accountInformation.setOrganizationId(rs.getLong("organization_id"));
                accountInformation.setSubscriptionId(rs.getLong("s_id"));
            }
        }
        if(accountInformation != null){
            list.add(accountInformation);
        }


        return list;
    }
}
