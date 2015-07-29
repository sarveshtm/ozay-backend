package com.ozay.repository;

import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.resultsetextractor.AccountResultSetExtractor;
import com.ozay.rowmapper.UserRowMapper;
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

        String query = "SELECT DISTINCT s.id as s_id, s.user_id as s_user_id, o.id as organization_id, rp.name\n" +
            "FROM t_user u " +
            "LEFT JOIN subscription s ON s.user_id = u.id " +
            "LEFT JOIN organization o ON o.user_id = s.user_id  " +
            "LEFT JOIN member m ON  u.id =  m.user_id " +
            "LEFT JOIN role_member rm ON rm.member_id = m.id " +
            "LEFT JOIN role_permission rp ON rp.role_id = rm.role_id " +
            "WHERE u.id = :id AND ((rp.role_id is not null ";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", user.getId());

        if(buildingId != null){
            query += "AND m.building_id = :buildingId";
            params.addValue("buildingId", buildingId);
        }

        query += ") OR o.id is not null)";


        List<AccountInformation> accountInformations = (List<AccountInformation>)namedParameterJdbcTemplate.query(query, params, new AccountResultSetExtractor());
        AccountInformation accountInformation = null;
        if(accountInformations.size() > 0){
            accountInformation = accountInformations.get(0);
        }


////        HashMap<String,Authority> map = new HashMap<String,Authority>();
//        System.out.println(account);


        return accountInformation;
    }

    public boolean isInvitedUser(String activationKey){
        String query = "SELECT * from t_user u INNER JOIN invited_user iu ON iu.id = u.id WHERE u.activation_key = :activationKey";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("activationKey", activationKey);
        List<User> list = namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
        if(list.size() == 0){
            return true;
        }
            return false;
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

    public void updateInvitedUser(User user){
        String query = "UPDATE USER SET login=:login, password=:password, activated=true WHERE activation_key = :activationKey";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
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
