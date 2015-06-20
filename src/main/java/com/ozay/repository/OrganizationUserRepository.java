package com.ozay.repository;

import com.ozay.domain.User;
import com.ozay.rowmapper.UserRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class OrganizationUserRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<User> findOrganizationUsers(Long organizationId){
        String query="SELECT u.* from t_user u INNER JOIN organization_user ou ON ou.user_id = u.id WHERE ou.organization_id = :organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        return namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
    }

    public User findOrganizationUser(Long organizationId, Long userId){
        String query="SELECT u.* from t_user u INNER JOIN organization_user ou ON ou.user_id = u.id WHERE ou.organization_id = :organizationId AND u.id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        params.addValue("userId", userId);
        List<User> list = namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
        if(list.size() == 1){
            return list.get(0);
        } else{
            return null;
        }
    }

    public void create(long userId, long organizationId){
        String query="INSERT INTO organization_user (user_id, organization_id) VALUES(:userId, :organizationId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("organizationId", organizationId);
        namedParameterJdbcTemplate.update(query,params);
    }

    public void delete(long userId, long organizationId){
        String query="DELETE FROM organization_user WHERE user_id:userId AND organization_id=:organizationId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("organizationId", organizationId);
        namedParameterJdbcTemplate.update(query,params);
    }
}
