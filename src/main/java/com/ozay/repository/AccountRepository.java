package com.ozay.repository;

import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.Account;
import com.ozay.model.Building;
import com.ozay.rowmapper.AccountMapper;
import com.ozay.rowmapper.BuildingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;


@Repository
public class AccountRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Account> getAllByBuildingId(long buildingId){
        String query = "SELECT * FROM account where building_id = :building_id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        return null;
    }

    public List<Account> getLoginUserInformation(String login, long buildingId){
        String query = "SELECT * FROM organization o" +
            "inner join building b on b.organization_id = o.id and b.id = :buildingId" +
            "inner join member m on m.building_id = b.id" +
            "inner join role_member rm on rm.member_id = m.id" +
            "inner join role r on rm.role_id = r.id " +
            "inner join role_access ra on ra.role_id = r.id" +
            "left join t_user u on u.id = m.user_id" +
            "left join organization_access oa on u.id = oa.user_id" +
            "WHERE u.login = :login";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", login);
        params.addValue("buildingId", buildingId);

        List<Account> accounts = namedParameterJdbcTemplate.query(query, params, new AccountMapper());

        Account account = null;
        HashMap<String,Authority> map = new HashMap<String,Authority>();
        for(Account tempAccount:accounts){
            if(account == null){
                account = new Account();
                account.setUserId(account.getUserId());
            }
            if(!map.containsKey(account.getAccess())){
                Authority authority = new Authority();
                authority.setName(tempAccount.getAccess());
                map.put(account.getAccess(), authority);
            }
        }
       // account.setAuthorities(map.values());

        return null;
    }



















    // These are for User and Authority due to Hibernate restriction
    public void insertUser(User user){
        String query = "INSERT INTO t_user (login, first_name, last_name, password, email, lang_key, created_by, activated, activation_key, created_date) " +
            "VALUES (:login, :firstName, :lastName, :password, :email, :lang_key, :createdBy, :activated, :activationKey, NOW())";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        parameterSource.addValue("login", user.getLogin());
        parameterSource.addValue("firstName", user.getFirstName());
        parameterSource.addValue("lastName", user.getLastName());
        parameterSource.addValue("password", user.getPassword());
        parameterSource.addValue("email", user.getEmail());
        parameterSource.addValue("lang_key", user.getLangKey());
        parameterSource.addValue("createdBy", user.getCreatedBy());
        parameterSource.addValue("activated", user.getActivated());
        parameterSource.addValue("activationKey", user.getActivationKey());
        namedParameterJdbcTemplate.update(query, parameterSource);
    }

    public void insertAuthority(Authority authority, String login){
        String query = "INSERT INTO t_user_authority (login, name) VALUES(:login, :name)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("login", login);
        parameterSource.addValue("name", authority.getName());
        namedParameterJdbcTemplate.update(query, parameterSource);
    }
}
