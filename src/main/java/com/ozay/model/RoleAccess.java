package com.ozay.model;

/**
 * Created by naofumiezaki on 6/10/15.
 */
public class RoleAccess {

    private long roleId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
