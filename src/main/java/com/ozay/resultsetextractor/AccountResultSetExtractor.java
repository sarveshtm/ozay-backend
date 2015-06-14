package com.ozay.resultsetextractor;

import com.ozay.domain.Authority;
import com.ozay.model.Account;
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
        List<Account> list = new ArrayList<Account>();
        Account account = null;
        HashMap<String,Authority> map = new HashMap<String,Authority>();
        while(rs.next()){
            if(account == null){
                account = new Account();
                account.setSubscriberId(rs.getLong("s_user_id"));
                account.setOrganizationId(rs.getLong("organization_id"));
            }
        }
        list.add(account);

        return list;
    }
}
