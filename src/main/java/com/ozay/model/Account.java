package com.ozay.model;

import com.ozay.domain.Authority;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Set;

/**
 * Created by naofumiezaki on 6/11/15.
 */
public class Account {
    private long userId;
    private boolean owner;
    private boolean organizationReady = true;
    private DateTime createdDate;
    private DateTime dateFrom;
    private DateTime dateTo;
    private String access;

    Collection<Authority> authorities;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isOrganizationReady() {
        return organizationReady;
    }

    public void setOrganizationReady(boolean organizationReady) {
        this.organizationReady = organizationReady;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(DateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public DateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(DateTime dateTo) {
        this.dateTo = dateTo;
    }


    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
