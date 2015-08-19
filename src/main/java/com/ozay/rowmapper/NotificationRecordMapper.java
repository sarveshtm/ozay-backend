package com.ozay.rowmapper;

import com.ozay.model.Member;
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

        Member member = new Member();

        notificationRecord.setMemberId(rs.getLong("member_id"));
        notificationRecord.setNotificationId(rs.getLong("notification_id"));
        notificationRecord.setEmail(rs.getString("email"));
        notificationRecord.setNote(rs.getString("note"));
        notificationRecord.setSuccess(rs.getBoolean("success"));

        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setUnit(rs.getString("unit"));

        notificationRecord.setMember(member);


        return notificationRecord;
    }
}
