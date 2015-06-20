package com.ozay.repository;

import com.ozay.model.Role;
import com.ozay.resultsetextractor.RoleResultSetExtractor;
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

    public Role findOne(long roleId){
        String query = "SELECT r.*, rp.name as rp_name FROM role r LEFT JOIN role_permission rp ON rp.role_id = r.id where r.id = :roleId";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("roleId", roleId);

        List<Role> roles =  (List<Role> )namedParameterJdbcTemplate.query(query, params, new RoleResultSetExtractor());
        if(roles.size() == 1){
            return roles.get(0);
        } else {
            return null;
        }

    }

    public Long create(Role role){
        String query="INSERT INTO role (building_id, name, sort_order, organization_user_role) VALUES(:buildingId, :name, :sortOrder, :organizationUserRole) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", role.getBuildingId());
        params.addValue("name", role.getName());
        params.addValue("sortOrder", role.getSortOrder());
        params.addValue("organizationUserRole", role.isOrganizationUserRole());
        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
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
