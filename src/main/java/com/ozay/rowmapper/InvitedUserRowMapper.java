package com.ozay.rowmapper;

import com.ozay.model.Building;
import com.ozay.model.InvitedUser;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 6/6/15.
 */
public class InvitedUserRowMapper implements RowMapper {
    public InvitedUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        InvitedUser invitedUser = new InvitedUser();
        invitedUser.setId(rs.getInt("id"));
        invitedUser.setMemberId(rs.getInt("member_id"));
        invitedUser.setLangKey(rs.getString("lang_key"));
        invitedUser.setActivated(rs.getBoolean("activated"));
        invitedUser.setActivationKey(rs.getString("activation_key"));
        invitedUser.setUpdatedBy(rs.getString("updated_by"));
        invitedUser.setCreatedBy(rs.getString("created_by"));
        invitedUser.setCreatedDate(new DateTime(rs.getTimestamp("created_date")));
        invitedUser.setActivatedDate(new DateTime(rs.getTimestamp("activated_date")));
        invitedUser.setUpdatedDate(new DateTime(rs.getTimestamp("updated_date")));
        return invitedUser;
    }
}
