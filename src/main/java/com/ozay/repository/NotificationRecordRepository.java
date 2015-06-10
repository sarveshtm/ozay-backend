package com.ozay.repository;

import com.ozay.model.NotificationRecord;
import com.ozay.rowmapper.NotificationRecordMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by naofumiezaki on 6/9/15.
 */
@Repository
public class NotificationRecordRepository {
    @Inject
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<NotificationRecord> getAllByNotificationId(Long notificationId){
        String query = "SELECT * FROM notification_record WHERE notification_id = :notificationId";

        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationId);

        return namedParameterJdbcTemplate.query(query, params, new NotificationRecordMapper());
    }

    public void create(NotificationRecord notificationRecord){
        String query = "INSERT INTO notification_record (notification_id, member_id, success, email, note) VALUES(:notificationId, :memberId, :success, :email, :note)";
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("notificationId", notificationRecord.getNotificationId());
        params.addValue("memberId", notificationRecord.getMemberId());
        params.addValue("success", notificationRecord.isSuccess());
        params.addValue("note", notificationRecord.getNote());
        params.addValue("email", notificationRecord.getEmail());

        namedParameterJdbcTemplate.update(query, params);
    }
}
