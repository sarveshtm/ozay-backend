package com.ozay.repository;

import com.ozay.model.OrganizationUserActivationKey;
import com.ozay.resultsetextractor.OrganizationUserActivationKeyResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
public class OrganizationUserActivationKeyRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrganizationUserActivationKey findByKey(String activationKey){
        OrganizationUserActivationKey organizationUserActivationKey = null;
        String query = "SELECT * FROM organization_user_activation_key WHERE activation_key = :key AND expired_date > NOW() AND used = false";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("key", activationKey);

        List<OrganizationUserActivationKey> list = (List<OrganizationUserActivationKey>)namedParameterJdbcTemplate.query(query, params, new OrganizationUserActivationKeyResultSetExtractor());
        if(list != null){
            organizationUserActivationKey = list.get(0);
        }
        return organizationUserActivationKey;
    }

    public void create(OrganizationUserActivationKey organizationUserActivationKey){
        String query = "INSERT INTO organization_user_activation_key (activation_key, created_by, created_date, expired_date, user_id) VALUES (:key, :createdBy, now(), now()  + interval '7 day', :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationUserActivationKey.getUserId());
        params.addValue("key", organizationUserActivationKey.getActivationKey());
        params.addValue("createdBy", organizationUserActivationKey.getCreatedBy());

        namedParameterJdbcTemplate.update(query, params);
    }

    // Update all record's used where user id is ?
    public void updateUsed(Long userId){
        String query = "UPDATE organization_user_activation_key set used = true WHERE user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        namedParameterJdbcTemplate.update(query, params);
    }
}
