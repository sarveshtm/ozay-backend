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
        return jdbcTemplate.query("SELECT * FROM building", new Object[]{}, new BuildingRowMapper(){

        });
    }

    public List<Building> getBuildingsByUser(String login){
        return jdbcTemplate.query("SELECT * FROM building b INNER JOIN user_building u ON b.id = u.building_id WHERE u.login = ?", new Object[]{login}, new BuildingRowMapper(){
        });
    }

    public Building getBuilding(int id){
        return (Building)jdbcTemplate.queryForObject("SELECT * FROM building WHERE id = ?", new Object[]{id}, new BuildingRowMapper(){
        });
    }

    public int create(Building building){

        String insert = "INSERT INTO building(" +
            "name, " +
            "email, " +
            "address_1, " +
            "address_2, " +
            "state, " +
            "zip, " +
            "phone, " +
            "created_by, " +
            "created_date, " +
            "last_modified_by," +
            "last_modified_date )" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now()) RETURNING id";
        Object[] params = new Object[] {
            building.getName(),
            building.getEmail(),
            building.getAddress_1(),
            building.getAddress_2(),
            building.getState(),
            building.getZip(),
            building.getPhone(),
            building.getCreatedBy(),
            building.getLastModifiedBy(),
        };

        int id = jdbcTemplate.queryForObject(insert, Integer.class, params);
        return id;
    }
}
