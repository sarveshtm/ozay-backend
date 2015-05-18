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
        building.setCreatedBy("created_by");
        building.setEmail(rs.getString("email"));
        building.setState(rs.getString("state"));
        building.setLastModifiedBy("last_modified_by");

        return building;
    }
}
