package com.ozay.model;

/**
 * Created by naofumiezaki on 6/10/15.
 */
public class RolePermission {

    private Long roleId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RolePermission rolePermission = (RolePermission) o;

        if (!roleId.equals(rolePermission.getRoleId()) && name.equals(rolePermission.getName())) {
            return false;
        }

        return true;
    }
    @Override
    public String toString() {
        return "RolePermission{" +
            "roleId='" + roleId + '\'' +
            "name='" + name + '\'' +

            "}";
    }
}
