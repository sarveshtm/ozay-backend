package com.ozay.repository;

import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.Account;
import com.ozay.model.Role;
import com.ozay.rowmapper.RoleMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class RoleRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Role> findAllByBuilding(long buildingId){
        String query = "SELECT * FROM role where building_id = :building_id";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("building_id", buildingId);

        return namedParameterJdbcTemplate.query(query, params, new RoleMapper());

    }

    public void create(Role role){
        String query="INSERT INTO role (building_id, name, sort_order) VALUES(:buildingId, :name, :sortOrder)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", role.getBuildingId());
        params.addValue("name", role.getName());
        params.addValue("sortOrder", role.getSortOrder());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(Role role){
        String query="UPDATE role SET building_id=:buildingId, name=:name, sort_order=:sortOrder WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", role.getBuildingId());
        params.addValue("name", role.getName());
        params.addValue("sortOrder", role.getSortOrder());
        params.addValue("id", role.getId());
        namedParameterJdbcTemplate.update(query,params);
    }





}
