package com.ozay.resultsetextractor;

import com.ozay.model.Account;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 6/12/15.
 */
public class AccountResultSetExtractor implements ResultSetExtractor<List<Account>> {

    public List<Account> extractData(ResultSet rs) throws SQLException{
        List<Account> accountList = new ArrayList<Account>();
        while(rs.next()){

        }
        return accountList;
    }
}
