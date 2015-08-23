package com.ozay.rowmapper;

import com.ozay.model.OrganizationUserActivationKey;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class OrganizationUserActivationKeyMapper implements RowMapper {

    public OrganizationUserActivationKey mapRow(ResultSet rs, int rowNum) throws SQLException {

        OrganizationUserActivationKey organizationUserActivationKey = new OrganizationUserActivationKey();
        organizationUserActivationKey.setId(rs.getLong("id"));
        organizationUserActivationKey.setUserId(rs.getLong("user_id"));
        organizationUserActivationKey.setActivationKey(rs.getLong("key"));

        return organizationUserActivationKey;
    }
}
