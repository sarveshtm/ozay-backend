package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Building;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.web.rest.dto.Dashboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private BuildingRepository buildingRepository;

    /**
     * GET  /rest/dashboard/building/{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/dashboard",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dashboard> getMemberDetail(@RequestParam(value = "building") Long buildingId) {
        Dashboard dashboard = new Dashboard();

        Building building = buildingRepository.getBuilding(buildingId);

        dashboard.setTotalUnits(building.getTotalUnits());
        dashboard.setActiveUnits(memberRepository.countActiveUnits(buildingId));

        return new ResponseEntity<Dashboard>(dashboard,  new HttpHeaders(), HttpStatus.OK);
    }
}
