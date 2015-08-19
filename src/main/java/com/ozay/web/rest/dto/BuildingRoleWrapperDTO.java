package com.ozay.web.rest.dto;

import com.ozay.model.Building;
import com.ozay.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 8/19/15.
 * Used management/organization/view/:organizationID page
 */
public class BuildingRoleWrapperDTO {
    private Building building;
    private List<Role> roleList;

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
