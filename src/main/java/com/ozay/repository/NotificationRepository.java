package com.ozay.repository;

import com.ozay.model.Notification;
import com.ozay.rowmapper.NotificationMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@Repository
public class NotificationRepository{

    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Notification> findAllByBuilding(Integer buildingId){
        String query = "SELECT * FROM notification where building_id = :buildingId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        return namedParameterJdbcTemplate.query(query, params, new NotificationMapper());
    };

    public Notification findOne(Long id){
        String query = "SELECT * FROM notification where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return (Notification)namedParameterJdbcTemplate.queryForObject(query, params, new NotificationMapper());
    }


    public List<Notification> searchNotification(int buildingId, String item){
        String query = "SELECT * FROM notification where building_id = :buildingId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", buildingId);
        return namedParameterJdbcTemplate.query(query, params, new NotificationMapper());
    };

    public Long create(Notification notification){
        String query = "INSERT INTO notification (building_id, notice, issue_date, created_by, created_date, subject, success)" +
            " VALUES (:buildingId, :notice, :issueDate, :createdBy, NOW(), :subject) RETURNING id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("buildingId", notification.getBuildingId());
        params.addValue("notice", notification.getNotice());
        params.addValue("issueDate", new Timestamp(notification.getIssueDate().getMillis()));
        params.addValue("createdBy", notification.getCreatedBy());
        params.addValue("subject", notification.getSubject());

        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }
}
