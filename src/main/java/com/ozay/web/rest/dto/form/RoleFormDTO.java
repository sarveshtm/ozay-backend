package com.ozay.web.rest.dto.form;

import com.ozay.model.Role;
import com.ozay.web.rest.dto.OrganizationUserRoleDTO;
import com.ozay.web.rest.dto.UserDTO;

import java.util.List;

/**
 * Created by naofumiezaki on 8/30/15.
 */
public class RoleFormDTO {
    private Role role;

    private List<OrganizationUserRoleDTO> organizationUserRoleDTOs;

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
