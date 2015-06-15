package com.ozay.rowmapper;

import com.ozay.model.AccountInformation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class AccountMapper implements RowMapper {

    public AccountInformation mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountInformation accountInformation = new AccountInformation();

        return accountInformation;
    }
}
