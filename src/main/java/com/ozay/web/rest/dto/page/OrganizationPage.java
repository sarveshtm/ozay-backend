package com.ozay.web.rest.dto.page;

import com.ozay.model.Building;
import com.ozay.model.Organization;
import com.ozay.web.rest.dto.BuildingRoleWrapperDTO;
import com.ozay.web.rest.dto.UserDTO;

import java.util.List;

/**
 * Created by naofumiezaki on 8/18/15.
 */
public class OrganizationPage {
    private Organization organization;
    private List<BuildingRoleWrapperDTO> buildingRoleWrapperDTOs;
    private List<UserDTO> userDTOs;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<BuildingRoleWrapperDTO> getBuildingRoleWrapperDTOs() {
        return buildingRoleWrapperDTOs;
    }

    public void setBuildingRoleWrapperDTOs(List<BuildingRoleWrapperDTO> buildingRoleWrapperDTOs) {
        this.buildingRoleWrapperDTOs = buildingRoleWrapperDTOs;
    }

    public List<UserDTO> getUserDTOs() {
        return userDTOs;
    }

    public void setUserDTOs(List<UserDTO> userDTOs) {
        this.userDTOs = userDTOs;
    }
}
