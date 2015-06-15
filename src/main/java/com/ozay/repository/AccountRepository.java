package com.ozay.repository;

import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.resultsetextractor.AccountResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;


@Repository
public class AccountRepository {

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<AccountInformation> getAllByBuildingId(long buildingId){
        String query = "SELECT * FROM account where building_id = :building_id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        return null;
    }

    public AccountInformation getLoginUserInformation(User user,Long buildingId){

        String query = "SELECT s.id as s_id, s.user_id as s_user_id, o.id as organization_id FROM t_user u LEFT JOIN subscription s ON s.user_id = u.id LEFT JOIN organization o ON o.user_id = s.user_id  WHERE u.id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", user.getId());
        //params.addValue("buildingId", buildingId);

        List<AccountInformation> accountInformations = (List<AccountInformation>)namedParameterJdbcTemplate.query(query, params, new AccountResultSetExtractor());
        AccountInformation accountInformation = null;
        if(accountInformations.size() > 0){
            accountInformation = accountInformations.get(0);
        }

////        HashMap<String,Authority> map = new HashMap<String,Authority>();
//        System.out.println(account);


        return accountInformation;
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
