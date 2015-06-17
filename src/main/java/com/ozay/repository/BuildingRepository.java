package com.ozay.repository;

import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.rowmapper.BuildingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class BuildingRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Building> getBuildings(){
        return namedParameterJdbcTemplate.query("SELECT * FROM building order by id", new BuildingRowMapper(){
        });
    }

    public List<Building> getBuildingsUserCanAccess(User user){
        String query = "SELECT b.* FROM building b " +
            "LEFT JOIN organization o ON o.id = b.organization_id " +
            "LEFT JOIN organization_user ou ON ou.organization_id = o.id " +
            "LEFT JOIN member m ON b.id = m.building_id " +
            "LEFT JOIN subscription s ON s.id = o.subscription_id " +
            "WHERE s.user_id = :userId OR m.user_id = :userId OR ou.user_id = :userId " +
            "GROUP BY b.id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", user.getId());
        return namedParameterJdbcTemplate.query(query, params, new BuildingRowMapper(){
        });
    }



//    public List<Building> getBuildingsByUser(long id){
//        String query = "SELECT * FROM building b INNER JOIN user_building u ON b.id = u.building_id WHERE u.user_id = :userId order by id";
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("userId", id);
//        return namedParameterJdbcTemplate.query(query, params, new BuildingRowMapper(){
//        });
//    }

    public List<Building> getBuildingsByOrganization(long organizationId){
        String query = "SELECT * FROM building WHERE organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return namedParameterJdbcTemplate.query(query, params, new BuildingRowMapper(){
        });
    }

    public Building getBuilding(long id){
        String query = "SELECT * FROM building WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Building> list =  namedParameterJdbcTemplate.query(query, params, new BuildingRowMapper(){});

        if(list.size() == 1){
            return list.get(0);
        }
        else {
            return null;
        }
    }

    public int create(Building building){

        String insert = "INSERT INTO building(" +
            "name, " +
            "organization_id, " +
            "email, " +
            "address_1, " +
            "address_2, " +
            "state, " +
            "zip, " +
            "phone, " +
            "total_units, " +
            "created_by, " +
            "created_date)" +
            "VALUES (:name, :organizationId, :email, :address1, :address2, :state, :zip, :phone, :totalUnits,:createdBy, now()) RETURNING id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", building.getName());
        params.addValue("organizationId", building.getOrganizationId());
        params.addValue("email", building.getEmail());
        params.addValue("address1", building.getAddress1());
        params.addValue("address2", building.getAddress2());
        params.addValue("state", building.getState());
        params.addValue("zip", building.getZip());
        params.addValue("phone", building.getPhone());
        params.addValue("totalUnits", building.getTotalUnits());
        params.addValue("createdBy", building.getCreatedBy());

        int id = namedParameterJdbcTemplate.queryForObject(insert, params, Integer.class );
        return id;
    }

    public void update(Building building){

        String query = "UPDATE building SET " +
            "name =:name, " +
            "organization_id = :organizationId, " +
            "email =:email, " +
            "address_1= :address1, " +
            "address_2 = :address2, " +
            "state=:state, " +
            "zip=:zip, " +
            "phone=:phone, " +
            "total_units=:totalUnits," +
            "last_modified_by =:modifiedBy, " +
            "last_modified_date=now() " +
            "WHERE id=:id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", building.getName());
        params.addValue("organizationId", building.getOrganizationId());
        params.addValue("email", building.getEmail());
        params.addValue("address1", building.getAddress1());
        params.addValue("address2", building.getAddress2());
        params.addValue("state", building.getState());
        params.addValue("zip", building.getZip());
        params.addValue("phone", building.getPhone());
        params.addValue("totalUnits", building.getTotalUnits());
        params.addValue("modifiedBy", building.getLastModifiedBy());
        params.addValue("id", building.getId());

        namedParameterJdbcTemplate.update(query, params);
    }
}
