package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.model.Building;
import com.ozay.model.Organization;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.BuildingRoleWrapperDTO;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.page.OrganizationSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api/page")
public class PageResource {

    private final Logger log = LoggerFactory.getLogger(PageResource.class);

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    BuildingRepository buildingRepository;

    @Inject
    RoleRepository roleRepository;


    /**
     * GET  /Organization -> get organizations.
     */
    @RequestMapping(value = "/organization/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationSummary>getOne(@PathVariable long id) {
        OrganizationSummary organizationSummary = new OrganizationSummary();
        organizationSummary.setOrganization(organizationRepository.findOne(id));
        organizationSummary.setBuildingRoleWrapperDTOs(new ArrayList<BuildingRoleWrapperDTO>());

        List<Building> buildings = buildingRepository.getBuildingsByOrganization(id);



        for(Building building : buildings){
            BuildingRoleWrapperDTO buildingRoleWrapperDTO = new BuildingRoleWrapperDTO();
            buildingRoleWrapperDTO.setBuilding(building);
            buildingRoleWrapperDTO.setRoleList(roleRepository.findAllByBuilding(building.getId()));
            organizationSummary.getBuildingRoleWrapperDTOs().add(buildingRoleWrapperDTO);
        }


        return new ResponseEntity<OrganizationSummary>(organizationSummary, HttpStatus.OK);
    }




}
