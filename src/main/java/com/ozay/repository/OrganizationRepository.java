package com.ozay.repository;

import com.ozay.model.Organization;
import com.ozay.model.Role;
import com.ozay.rowmapper.RoleMapper;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;


@Repository
public class OrganizationRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Organization> findAllByUserId(long buildingId){
        String query = "SELECT * FROM organization where user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();

        return namedParameterJdbcTemplate.query(query, params, new RoleMapper());

    }

    public void create(Organization organization){
        String query = "INSERT INTO organization (user_id, name, created_date, address_1, address_2, phone, state, country, zip) " +
            "VALUES(:userId, :name, :createdDate, :address1, :address2, phone, state, country, zip)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("createdDate", new Timestamp(organization.getCreatedDate().getMillis()));
        params.addValue("address1", organization.getAddress1());
        params.addValue("address2", organization.getAddress2());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        namedParameterJdbcTemplate.update(query,params);

    }

    public void update(Organization organization){
        String query = "UPDATE organization " +
            "SET user_id=:userId, " +
            "name=:name, " +
            "created_date=:createdDate," +
            "address_1=:address1," +
            "address_2=:address2," +
            "phone=:phone," +
            "state=:state," +
            "country=:country," +
            "zip=:zip" +
            "WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("createdDate", new Timestamp(organization.getCreatedDate().getMillis()));
        params.addValue("address1", organization.getAddress1());
        params.addValue("address2", organization.getAddress2());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        params.addValue("id", organization.getId());
        namedParameterJdbcTemplate.update(query,params);
    }






}
