package com.ozay.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ozay.model.Building;
import org.springframework.jdbc.core.RowMapper;
/**
 * Created by naofumiezaki on 4/29/15.
 */
public class BuildingRowMapper implements RowMapper {

    public Building mapRow(ResultSet rs, int rowNum) throws SQLException {
        Building building = new Building();
        building.setId(rs.getInt("id"));
        building.setName(rs.getString("name"));
        building.setAddress_1("address_1");
        building.setAddress_2("address_2");
        building.setState("state");
        building.setZip("zip");
        building.setPhone("phone");
        building.setCreatedBy(rs.getInt("created_by"));
        building.setEmail(rs.getString("email"));
        building.setState(rs.getString("state"));
        building.setLastModifiedBy(rs.getInt("last_modified_by"));
        building.setTotalUnits(rs.getInt("total_units"));

        return building;
    }
}
