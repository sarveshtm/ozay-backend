package com.ozay.rowmapper;

import com.ozay.model.Building;
import com.ozay.model.NotificationRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 4/29/15.
 */
public class NotificationRecordMapper implements RowMapper {

    public NotificationRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        NotificationRecord notificationRecord = new NotificationRecord();

        notificationRecord.setMemberId(rs.getLong("user_id"));
        notificationRecord.setNotificationId(rs.getLong("notification_id"));
        notificationRecord.setEmail(rs.getString("email"));
        notificationRecord.setNote(rs.getString("note"));
        notificationRecord.setSuccess(rs.getBoolean("success"));

        return notificationRecord;
    }
}
