package com.ozay.rowmapper;

import com.ozay.model.InvitedMember;
import org.joda.time.DateTime;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by naofumiezaki on 6/6/15.
 */
public class InvitedMemberRowMapper implements RowMapper {
    public InvitedMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        InvitedMember invitedMember = new InvitedMember();
        invitedMember.setId(rs.getInt("id"));
        invitedMember.setMemberId(rs.getInt("member_id"));
        invitedMember.setLangKey(rs.getString("lang_key"));
        invitedMember.setActivated(rs.getBoolean("activated"));
        invitedMember.setActivationKey(rs.getString("activation_key"));
        invitedMember.setUpdatedBy(rs.getString("updated_by"));
        invitedMember.setCreatedBy(rs.getString("created_by"));
        invitedMember.setCreatedDate(new DateTime(rs.getTimestamp("created_date")));
        invitedMember.setActivatedDate(new DateTime(rs.getTimestamp("activated_date")));
        invitedMember.setUpdatedDate(new DateTime(rs.getTimestamp("updated_date")));
        return invitedMember;
    }
}
