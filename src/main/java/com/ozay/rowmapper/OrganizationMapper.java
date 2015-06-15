package com.ozay.rowmapper;

import com.ozay.model.Organization;
import com.ozay.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class OrganizationMapper implements RowMapper {

    public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
        Organization organization = new Organization();
        organization.setId(rs.getLong("id"));
        organization.setUserId(rs.getLong("user_id"));
        organization.setName(rs.getString("name"));
        organization.setAddress1(rs.getString("address_1"));
        organization.setAddress2(rs.getString("address_2"));
        organization.setPhone(rs.getString("phone"));
        organization.setCountry(rs.getString("country"));
        organization.setState(rs.getString("state"));
        organization.setSubscriptionId(rs.getLong("subscription_id"));

        return organization;
    }
}
