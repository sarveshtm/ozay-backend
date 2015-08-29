package com.ozay.rowmapper;

import com.ozay.model.Building;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Created by naofumiezaki on 4/29/15.
 */
public class BuildingRowMapper implements RowMapper {

    public Building mapRow(ResultSet rs, int rowNum) throws SQLException {

        Building building = new Building();
        building.setId(rs.getLong("id"));
        building.setName(rs.getString("name"));
        building.setAddress1("address_1");
        building.setAddress2("address_2");
        building.setState("state");
        building.setZip("zip");
        building.setPhone("phone");
        building.setCreatedBy(rs.getLong("created_by"));
        building.setEmail(rs.getString("email"));
        building.setState(rs.getString("state"));
        building.setLastModifiedBy(rs.getLong("last_modified_by"));
        building.setTotalUnits(rs.getInt("total_units"));
        building.setOrganizationId(rs.getLong("organization_id"));

        return building;
    }
}
