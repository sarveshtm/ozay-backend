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






}
