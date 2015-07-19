package com.ozay.repository;

import com.ozay.domain.User;
import com.ozay.rowmapper.UserRowMapper;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ozay.resultsetextractor.OrgRoleResultSetExtractor;
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

    public OrganizationUserDTO findOrganizationUser(long userId, long organizationId){
        String query="SELECT u.*,acc.name as org_permission,acc.organization_id from t_user u " +
            "INNER JOIN organization_user ou ON ou.user_id = u.id " +
            "LEFT JOIN organization_access acc ON ou.user_id = acc.user_id and ou.organization_id = acc.organization_id " +
            "WHERE ou.organization_id = :organizationId AND u.id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("organizationId", organizationId);
        params.addValue("userId", userId);
        //List<User> list = namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
        List<OrganizationUserDTO> roles =  (List<OrganizationUserDTO> )namedParameterJdbcTemplate.query(query, params, new OrgRoleResultSetExtractor());
        if(roles.size() == 1){
            return roles.get(0);
        } else {
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
