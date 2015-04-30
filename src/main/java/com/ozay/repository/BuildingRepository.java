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
        return jdbcTemplate.query("select * from building", new Object[]{}, new BuildingRowMapper(){

        });
    }


}
