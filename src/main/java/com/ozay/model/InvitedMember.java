package com.ozay.model;

import org.joda.time.DateTime;

/**
 * Created by naofumiezaki on 6/6/15.
 */
public class InvitedMember {
    private Integer id;
    private Integer memberId;
    private String langKey;
    private String activationKey;
    private boolean activated;
    private String createdBy;
    private DateTime createdDate;
    private String updatedBy;
    private DateTime updatedDate;
    private DateTime activatedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public DateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public DateTime getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(DateTime activatedDate) {
        this.activatedDate = activatedDate;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "InvitedUser{" +
            "id='" + id + '\'' +
            "memberId='" + memberId + '\'' +
            "activationKey='" + activationKey + '\'' +
            "activated='" + activated + '\'' +
            "}";
    }

}
