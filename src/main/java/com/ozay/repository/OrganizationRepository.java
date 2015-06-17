package com.ozay.repository;

import com.ozay.domain.User;
import com.ozay.model.Organization;
import com.ozay.model.Role;
import com.ozay.rowmapper.OrganizationMapper;
import com.ozay.rowmapper.RoleMapper;
import com.ozay.rowmapper.UserRowMapper;
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

    public List<Organization> findAllByUserId(long userId){
        String query = "SELECT * FROM organization where user_id = :user_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        return namedParameterJdbcTemplate.query(query, params, new OrganizationMapper());
    }

    public Organization findOne(long organizationid){
        String query = "SELECT * FROM organization where id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationid);
        List<Organization> list = namedParameterJdbcTemplate.query(query, params, new OrganizationMapper());
        if(list != null &&  list.size() > 0){
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<User> getOrganizationUsers(long organizationId){
        String query = "SELECT u.* FROM organization_user ou INNER JOIN t_user u ON ou.user_id = u.id where organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
    }

    public void create(Organization organization){
        String query = "INSERT INTO organization (user_id, name, created_date, address_1, address_2, phone, state, country, zip, subscription_id, created_by) " +
            "VALUES(:userId, :name, now(), :address1, :address2, :phone, :state, :country, :zip, :subscriptionId, :createdBy" +
            ")";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("address1", organization.getAddress1());
        params.addValue("address2", organization.getAddress2());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        params.addValue("subscriptionId", organization.getSubscriptionId());
        params.addValue("createdBy", organization.getCreatedBy());
        namedParameterJdbcTemplate.update(query,params);
    }

    public void update(Organization organization){
        String query = "UPDATE organization " +
            "SET user_id=:userId, " +
            "name=:name, " +
            "address_1=:address1," +
            "address_2=:address2," +
            "phone=:phone," +
            "state=:state," +
            "country=:country," +
            "zip=:zip, " +
            "modified_date=now(), " +
            "modified_by=:modifiedBy, " +
            "subscription_id=:subscriptionId " +
            "WHERE id=:id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organization.getUserId());
        params.addValue("name", organization.getName());
        params.addValue("address1", organization.getAddress1());
        params.addValue("address2", organization.getAddress2());
        params.addValue("phone", organization.getPhone());
        params.addValue("state", organization.getState());
        params.addValue("country", organization.getCountry());
        params.addValue("zip", organization.getZip());
        params.addValue("modifiedBy", organization.getModifiedBy());
        params.addValue("subscriptionId", organization.getSubscriptionId());
        params.addValue("id", organization.getId());
        namedParameterJdbcTemplate.update(query,params);
    }

}
