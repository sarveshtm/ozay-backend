package com.ozay.repository;

import com.ozay.model.Building;
import com.ozay.rowmapper.BuildingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class BuildingRepository {

    @Inject
    private JdbcTemplate jdbcTemplate;

    public List<Building> getBuildings(){
        return jdbcTemplate.query("SELECT * FROM building order by id", new Object[]{}, new BuildingRowMapper(){

        });
    }

    public List<Building> getBuildingsByUser(int id){
        return jdbcTemplate.query("SELECT * FROM building b INNER JOIN user_building u ON b.id = u.building_id WHERE u.user_id = ? order by id", new Object[]{id}, new BuildingRowMapper(){
        });
    }

    public Building getBuilding(int id){
        return (Building)jdbcTemplate.queryForObject("SELECT * FROM building WHERE id = ?", new Object[]{id}, new BuildingRowMapper(){
        });
    }

    public int create(Building building){

        String insert = "INSERT INTO building(" +
            "name, " +
            "account_id, " +
            "email, " +
            "address_1, " +
            "address_2, " +
            "state, " +
            "zip, " +
            "phone, " +
            "total_units, " +
            "created_by, " +
            "created_date, " +
            "last_modified_by," +
            "last_modified_date )" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now()) RETURNING id";
        Object[] params = new Object[] {
            building.getName(),
            building.getAccountId(),
            building.getEmail(),
            building.getAddress1(),
            building.getAddress2(),
            building.getState(),
            building.getZip(),
            building.getPhone(),
            building.getTotalUnits(),
            building.getCreatedBy(),
            building.getLastModifiedBy(),
        };

        int id = jdbcTemplate.queryForObject(insert, Integer.class, params);
        return id;
    }
}
