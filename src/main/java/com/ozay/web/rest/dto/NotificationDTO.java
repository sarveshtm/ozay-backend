package com.ozay.web.rest.dto;

import com.ozay.model.Member;
import com.ozay.model.Role;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by naofumiezaki on 3/8/15.
 * Test
 */
public class NotificationDTO {
    private int buildingId;
    private DateTime issueDate;
    private String subject;
    private String notice;
    private Set<Role> roles;
    private Set<Integer> memberIds;

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public DateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public String toString() {
        return "NotifiactionDTO{" +


            "}";
    }
}
