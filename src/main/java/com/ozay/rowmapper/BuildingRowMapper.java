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
        return building;
    }
}
