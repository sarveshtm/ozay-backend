package com.ozay.web.rest.dto.page;

import com.ozay.domain.User;
import com.ozay.model.Permission;
import com.ozay.model.Role;
import com.ozay.web.rest.dto.OrganizationUserRoleDTO;

import java.util.List;

/**
 * Created by naofumiezaki on 8/30/15.
 */
public class RolePage {
    List<Permission> permissions;
    List<Role> roles;
    List<OrganizationUserRoleDTO>organizationUserRoleDTOs;
    Role role;

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<OrganizationUserRoleDTO> getOrganizationUserRoleDTOs() {
        return organizationUserRoleDTOs;
    }

    public void setOrganizationUserRoleDTOs(List<OrganizationUserRoleDTO> organizationUserRoleDTOs) {
        this.organizationUserRoleDTOs = organizationUserRoleDTOs;
    }
}
