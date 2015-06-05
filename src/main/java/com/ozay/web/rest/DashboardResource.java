package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Building;
import com.ozay.model.UserDetail;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.service.UserDetailService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.Dashboard;
import com.ozay.web.rest.dto.FieldErrorDTO;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.UserDetailListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);

    @Inject
    private UserDetailRepository userDetailRepository;

    @Inject
    private BuildingRepository buildingRepository;

    /**
     * GET  /rest/dashboard/building/{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/dashboard/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dashboard> getUserDetail(@PathVariable int buildingId) {
        Dashboard dashboard = new Dashboard();

        Building building = buildingRepository.getBuilding(buildingId);

        dashboard.setTotalUnits(building.getTotalUnits());
        dashboard.setActiveUnits(userDetailRepository.countActiveUnits(buildingId));

        return new ResponseEntity<Dashboard>(dashboard,  new HttpHeaders(), HttpStatus.OK);
    }
}
