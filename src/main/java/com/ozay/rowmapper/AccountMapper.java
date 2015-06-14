package com.ozay.rowmapper;

import com.ozay.model.Account;
import com.ozay.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class AccountMapper implements RowMapper {

    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();

        return account;
    }
}
