package com.ozay.model;

import java.util.Set;

/**
 * Created by naofumiezaki on 6/13/15.
 */
public class OrganizationPermission {
    private long userId;
    private long organizationId;
    private String name;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
