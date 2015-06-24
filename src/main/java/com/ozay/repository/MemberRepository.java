package com.ozay.repository;

import com.ozay.model.Member;
import com.ozay.resultsetextractor.MemberResultSetExtractor;
import com.ozay.rowmapper.MemberRowMapper;
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


@Repository
public class MemberRepository {
    private final Logger log = LoggerFactory.getLogger(Member.class);
    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Member findOne(Long id){
        String query = "SELECT * FROM member WHERE id =:id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        List<Member> list = (List<Member>)namedParameterJdbcTemplate.query("SELECT m.*, " +
            "r.id as r_id, " +
            "r.name as r_name, " +
            "r.building_id as r_building_id, " +
            "r.sort_order as r_sort_order, " +
            "r.organization_user_role as r_organization_user_role, " +
            "r.belong_to as r_belong_to " +
            " FROM member m LEFT JOIN role_member rm ON m.id = rm.member_id LEFT JOIN role r ON r.id = rm.role_id WHERE m.id =:id", parameterSource, new MemberResultSetExtractor());
        if(list.size()  == 1){
            return list.get(0);
        } else {
            return null;
        }
    }

    public List<Member> getAllUsersByBuilding(int buildingId){
        return jdbcTemplate.query("Select * FROM member WHERE building_id =? AND deleted = false",

            new Object[]{buildingId}, new MemberRowMapper() {

            });
    }

    public Integer countActiveUnits(int buildingId){
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT unit) FROM member WHERE building_id = ?", Integer.class, new Object[]{buildingId});
    }

    public List<Member> getUserByBuildingEmailUnit(int buildingId, String email, String unit){
        return jdbcTemplate.query("Select * FROM member WHERE building_id = ? AND email = ? AND UPPER(unit) = ? AND deleted = false",
            new Object[]{buildingId, email, unit}, new MemberRowMapper() {
            });
    }



    public List<Member> getUserEmailsForNotification(NotificationDTO notificationDTO){

        String query = "Select * FROM member WHERE building_id = :buildingId AND deleted = false ";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();

        parameterSource.addValue("buildingId", notificationDTO.getBuildingId());
        List<String> whereClauses = new ArrayList<String>();

        if(notificationDTO.isManagement() == true){
            whereClauses.add(" management = :management ");
            parameterSource.addValue("management", notificationDTO.isManagement());
        }
        if(notificationDTO.isStaff() == true){
            whereClauses.add(" staff = :staff ");
            parameterSource.addValue("staff", notificationDTO.isStaff());

        }
        if(notificationDTO.isBoard() == true){
            whereClauses.add(" board = :board ");

            parameterSource.addValue("board", notificationDTO.isBoard());
        }
        if(notificationDTO.isResident() == true){
            whereClauses.add(" resident = :resident ");
            parameterSource.addValue("resident", notificationDTO.isResident());
        }


        boolean hasIds = false;
        if(notificationDTO.isIndividual() == true && notificationDTO.getIndividuals().size() > 0){
            log.debug("Notification individual id : {}", notificationDTO.getIndividuals());
            hasIds = true;
            parameterSource.addValue("ids", notificationDTO.getIndividuals());
        }
        String restQuery = "";
        if(whereClauses.size() > 0 || (notificationDTO.isIndividual() == true && notificationDTO.getIndividuals().size() > 0)){
            boolean hasAndQuery = false;
            restQuery += " AND ( ";
            for(int i = 0; i<whereClauses.size(); i++){
                hasAndQuery = true;
                if(i != 0){
                    restQuery += " AND ";
                }
                restQuery += whereClauses.get(i);
            }
            if(hasIds == true){
                if(hasAndQuery == true){
                    restQuery += " OR ";
                }
                restQuery += " id IN ( :ids ) ";
            }

            restQuery += " ) ";
        }
        log.debug("Notification individual query : {}", query + restQuery);



        return namedParameterJdbcTemplate.query(query + restQuery,
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

    public List<Member> searchUsers(int buildingId, String item){
        String likeItem = "%" + item.toLowerCase() + "%";
        return jdbcTemplate.query("Select * FROM member " +
                "WHERE building_id =? AND " +
                "(LOWER(first_name) LIKE ? " +
                "OR LOWER(last_name) LIKE ? " +
                "OR LOWER(phone) LIKE ? " +
                "OR LOWER(email) LIKE ? " +
                "OR LOWER(unit) LIKE ? " +
                "OR LOWER(parking) LIKE ?)",

            new Object[]{buildingId, item, likeItem, likeItem, likeItem, likeItem, likeItem}, new MemberRowMapper(){

            });
    }

    public Long create(Member member){
        String insert = "INSERT INTO member(" +
            "login, " +
            "user_id, " +
            "first_name," +
            "last_name," +
            "email," +
            "phone," +
            "building_id, " +
            "ownership, " +
            "renter, " +
            "unit, " +
            "expiration_date, " +
            "parking)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        Object[] params = new Object[] { member.getLogin(),
            member.getUserId(),
            member.getFirstName(),
            member.getLastName(),
            member.getEmail(),
            member.getPhone(),
            member.getBuildingId(),
            member.getOwnership(),
            member.isRenter(),
            member.getUnit(),
            member.getExpirationDate(),
            member.getParking()};

        return jdbcTemplate.queryForObject(insert, params, Long.class);


    }
    public void update(Member member){
        String update = "UPDATE member SET " +
            "first_name = ?, " +
            "last_name = ?, " +
            "email = ?, " +
            "phone = ?, " +
            "ownership = ?, " +
            "renter = ?, " +
            "unit = ?, " +
            "expiration_date = ?, " +
            "parking = ?, " +
            "deleted = ?, " +
            "user_id = ? " +
            "WHERE building_id = ? " +
            "AND id = ?";

        Object[] params = new Object[] {
            member.getFirstName(),
            member.getLastName(),
            member.getEmail(),
            member.getPhone(),
            member.getOwnership(),
            member.isRenter(),
            member.getUnit(),
            member.getExpirationDate(),
            member.getParking(),
            member.isDeleted(),
            member.getUserId(),
            member.getBuildingId(),
            member.getId()
        };

        jdbcTemplate.update(update, params);

    }

}
