package com.ozay.repository;

import com.ozay.model.OrganizationUserActivationKey;
import com.ozay.resultsetextractor.OrganizationUserActivationKeyResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import java.util.List;

public class OrganizationUserActivationKeyRepoistory {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public OrganizationUserActivationKey findByKey(Long userId, Long activationKey){
        OrganizationUserActivationKey organizationUserActivationKey = null;
        String query = "SELECT * FROM organization_user_activation_key WHERE user_id = :userId AND key = :key AND expired_date >+ NOW()";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("key", activationKey);

        List<OrganizationUserActivationKey> list = (List<OrganizationUserActivationKey>)namedParameterJdbcTemplate.query(query, params, new OrganizationUserActivationKeyResultSetExtractor());
        if(list != null){
            organizationUserActivationKey = list.get(0);
        }
        return organizationUserActivationKey;
    }

    public void create(OrganizationUserActivationKey organizationUserActivationKey){
        String query = "INSERT INTO organization_user_activation_key (key, created_by, created_date, expired_date, user_id ) VALUES(:key, :createdBy, now(), now()  + interval '7 day', :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", organizationUserActivationKey.getUserId());
        params.addValue("key", organizationUserActivationKey.getActivationKey());
        params.addValue("createdBy", organizationUserActivationKey.getCreatedBy());
    }
}
