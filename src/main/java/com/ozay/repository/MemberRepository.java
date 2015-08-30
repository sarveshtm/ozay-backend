package com.ozay.repository;

import com.ozay.model.Member;
import com.ozay.resultsetextractor.MemberResultSetExtractor;
import com.ozay.rowmapper.MemberRowMapper;
import com.ozay.rowmapper.NotificationMapper;
import com.ozay.web.rest.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
public class MemberRepository {
    private final Logger log = LoggerFactory.getLogger(Member.class);
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Member findOne(Long id){

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        List<Member> list = (List<Member>)namedParameterJdbcTemplate.query("SELECT m.*, u.email as u_email," +
            "r.id as r_id, " +
            "r.name as r_name, " +
            "r.building_id as r_building_id, " +
            "r.sort_order as r_sort_order, " +
            "r.organization_user_role as r_organization_user_role, " +
            "r.belong_to as r_belong_to, " +
            "ou.user_id as organization_user_id " +
            "FROM member m LEFT JOIN t_user u ON u.id = m.user_id LEFT JOIN role_member rm ON m.id = rm.member_id LEFT JOIN role r ON r.id = rm.role_id INNER JOIN building b ON b.id = m.building_id INNER JOIN organization o ON o.id = b.organization_id LEFT JOIN organization_user ou ON ou.user_id = m.user_id WHERE m.id =:id", parameterSource, new MemberResultSetExtractor());
        if(list.size()  == 1){
            return list.get(0);
        } else {
            return null;
        }
    }

    public Member findOneByUserIdAndBuildingId(Long userId, Long buildingId){

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("userId", userId);
        parameterSource.addValue("buildingId", buildingId);


        List<Member> list = (List<Member>)namedParameterJdbcTemplate.query("SELECT m.*, u.email as u_email," +
            "r.id as r_id, " +
            "r.name as r_name, " +
            "r.building_id as r_building_id, " +
            "r.sort_order as r_sort_order, " +
            "r.organization_user_role as r_organization_user_role, " +
            "r.belong_to as r_belong_to, " +
            "ou.user_id as organization_user_id " +
            "FROM member m LEFT JOIN t_user u ON u.id = m.user_id LEFT JOIN role_member rm ON m.id = rm.member_id LEFT JOIN role r ON r.id = rm.role_id INNER JOIN building b ON b.id = m.building_id INNER JOIN organization o ON o.id = b.organization_id LEFT JOIN organization_user ou ON ou.user_id = m.user_id WHERE m.user_id =:userId AND m.building_id = :buildingId", parameterSource, new MemberResultSetExtractor());
        if(list.size()  == 1){
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Member> getAllMembersByBuilding(long buildingId){
        String query = "SELECT m.*,  FROM member WHERE building_id =:buildingId AND deleted = false";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("buildingId", buildingId);

        List<Member> list = (List<Member>)namedParameterJdbcTemplate.query("SELECT m.*, u.email as u_email, " +
            "r.id as r_id, " +
            "r.name as r_name, " +
            "r.building_id as r_building_id, " +
            "r.sort_order as r_sort_order, " +
            "r.organization_user_role as r_organization_user_role, " +
            "r.belong_to as r_belong_to, " +
            "ou.user_id as organization_user_id " +
            "FROM member m LEFT JOIN t_user u ON u.id = m.user_id LEFT JOIN role_member rm ON m.id = rm.member_id LEFT JOIN role r ON r.id = rm.role_id INNER JOIN building b ON b.id = m.building_id INNER JOIN organization o ON o.id = b.organization_id LEFT JOIN organization_user ou ON ou.user_id = m.user_id WHERE m.building_id = :buildingId AND m.deleted = false ORDER BY m.id", parameterSource, new MemberResultSetExtractor());


        return list;
    }

    public Integer countActiveUnits(Long buildingId){
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT unit) FROM member WHERE building_id = ? and deleted = false", Integer.class, new Object[]{buildingId});
    }

    public List<Member> getUserByBuildingEmailUnit(Long buildingId, String email, String unit){
        return jdbcTemplate.query("Select m.*, u.email as u_email FROM member m LEFT JOIN t_user u ON u.id = m.user_id WHERE m.building_id = ? AND m.email = ? AND UPPER(m.unit) = ? AND deleted = false",
            new Object[]{buildingId, email, unit}, new MemberRowMapper() {
            });
    }



    public List<Member> getUserEmailsForNotification(NotificationDTO notificationDTO){

        String query = "Select m.*, u.email as u_email FROM member m LEFT JOIN t_user u ON u.id = m.user_id WHERE building_id = :buildingId AND deleted = false AND m.id IN ( :ids )";
        StringBuilder sb = new StringBuilder();


        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("buildingId", notificationDTO.getBuildingId());
        parameterSource.addValue("ids", notificationDTO.getMemberIds());


        return namedParameterJdbcTemplate.query(query,
            parameterSource ,  new MemberRowMapper() );
    }


//    public Member getUserByBuilding(int user_id, int buildingId){
//        return (Member)jdbcTemplate.queryForObject("Select * FROM member WHERE building_id = ? AND id = ?",
//            new Object[]{buildingId, user_id}, new MemberRowMapper(){
//            });
//    }

//    public Member getMemberDetailByBuildingAndUserId(long id, long buildingId){
//        return (Member)jdbcTemplate.queryForObject("Select * FROM member WHERE building_id = ? AND user_id = ?",
//            new Object[]{buildingId, id}, new MemberRowMapper(){
//            });
//    }

    public Member getUserByBuilding(String login, int buildingId){
        return (Member)jdbcTemplate.queryForObject("Select * FROM member WHERE building_id = ? AND login = ?",
            new Object[]{buildingId, login}, new MemberRowMapper(){
            });
    }

    public List<Member> searchUsers(Long buildingId, String[] items){
        String query ="Select m.*, u.email as u_email FROM member m LEFT JOIN t_user u ON u.id = m.user_id WHERE deleted = false AND building_id =:buildingId ";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);

        String queryForList = "";
        for(int i = 0; i< items.length;i++){
            String param = "param" + i;
            params.addValue(param, items[i]);
            if(i != 0){
                queryForList += " OR ";
            }
            queryForList += " LOWER(m.first_name) LIKE :" + param +
                " OR LOWER(m.last_name) LIKE :" + param +
                " OR LOWER(m.phone) LIKE :" + param +
                " OR LOWER(m.email) LIKE :" + param +
                " OR LOWER(m.unit) LIKE :" + param +
                " OR LOWER(m.parking) LIKE :" + param;
        }
        if(items.length > 0){
            query += " AND (";
            query += queryForList;
            query += ")";
        }


        return namedParameterJdbcTemplate.query(query, params, new MemberRowMapper());
    }

    public void create(Member member){


        String insert = "INSERT INTO member(" +
            "login, " +
            "user_id, " +
            "first_name," +
            "last_name," +
            "email," +
            "phone," +
            "building_id, " +
            "ownership, " +
            "unit, " +
            "expiration_date, " +
            "parking)" +
            "VALUES (:login, :userId, :firstName, :lastName, :email, :phone, :buildingId, :ownership, :unit, :expirationDate, :parking) RETURNING id";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("login", member.getLogin());
        params.addValue("userId", member.getUserId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("email", member.getEmail());
        params.addValue("phone", member.getPhone());
        params.addValue("unit", member.getUnit());
        params.addValue("buildingId", member.getBuildingId());
        params.addValue("ownership", member.getOwnership());
        params.addValue("expirationDate", member.getExpirationDate());
        params.addValue("parking", member.getParking());




        Long id = namedParameterJdbcTemplate.queryForObject(insert, params, Long.class);
        member.setId(id);

    }
    public void update(Member member){
        String update = "UPDATE member SET " +
            "first_name = :firstName, " +
            "last_name = :lastName, " +
            "email = :email, " +
            "phone = :phone, " +
            "ownership = :ownership, " +
            "unit = :unit, " +
            "expiration_date = :expirationDate, " +
            "parking = :parking, " +
            "deleted = :deleted, " +
            "user_id = :userId " +
            "WHERE building_id = :buildingId " +
            "AND id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", member.getId());
        params.addValue("userId", member.getUserId());
        params.addValue("firstName", member.getFirstName());
        params.addValue("lastName", member.getLastName());
        params.addValue("email", member.getEmail());
        params.addValue("phone", member.getPhone());
        params.addValue("unit", member.getUnit());
        params.addValue("buildingId", member.getBuildingId());
        params.addValue("ownership", member.getOwnership());
        params.addValue("expirationDate", member.getExpirationDate());
        params.addValue("parking", member.getParking());
        params.addValue("deleted", member.isDeleted());

        namedParameterJdbcTemplate.update(update, params);

    }

}
