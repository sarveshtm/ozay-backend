package com.ozay.model;

import java.util.Set;

/**
 * Created by naofumiezaki on 6/10/15.
 */
public class Role {
    private long id;
    private String name;
    private long buildingId;
    private long sortOrder;


    private Set<RoleAccess> roleAccesses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(long buildingId) {
        this.buildingId = buildingId;
    }

    public long getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(long sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Set<RoleAccess> getRoleAccesses() {
        return roleAccesses;
    }

    public void setRoleAccesses(Set<RoleAccess> roleAccesses) {
        this.roleAccesses = roleAccesses;
    }
}
