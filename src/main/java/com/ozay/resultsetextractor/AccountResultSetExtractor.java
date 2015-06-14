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

    public Account extractData(ResultSet rs) throws SQLException{
        Account account = null;
        HashMap<String,Authority> map = new HashMap<String,Authority>();
        while(rs.next()){
            if(account == null){
                account = new Account();
                account.setUserId(rs.getLong("id"));
            }
            if(!map.containsKey(account.getAccess())){
                Authority authority = new Authority();
                authority.setName(rs.getString("name"));
            }
            if(!map.containsKey(account.getAccess())){
                Authority authority = new Authority();
                authority.setName(rs.getString("name"));
            }
        }

        return account;
    }
}
