package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Building;
import com.ozay.model.UserDetail;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.UserDetailService;
import com.ozay.web.rest.dto.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class BuildingResource {

    @Inject
    private UserDetailRepository userDetailRepository;
    @Inject
    private BuildingRepository buildingRepository;
    @Inject
    private UserDetailService userDetailService;
    @Inject
    private UserBuildingRepository userBuildingRepository;
    @Inject
    private UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);


    /**
     * GET  /rest/building/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/building",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Building> getAll() {
        log.debug("REST request to get building by user");
        log.debug("REST GET LOGIN USER : {}", SecurityUtils.getCurrentLogin());
        return buildingRepository.getBuildingsByUser(SecurityUtils.getCurrentLogin());
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/building",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Building building) {
        log.debug("REST request : Building create function");
        building.setCreatedBy(SecurityUtils.getCurrentLogin());
        building.setLastModifiedBy(SecurityUtils.getCurrentLogin());
        Integer insertedId = buildingRepository.create(building);
        log.debug("REST request : Building insertedId " + insertedId);
        JsonResponse json = new JsonResponse();
        if(insertedId > 0){
            UserDetail userDetail = new UserDetail();
            userDetail.setLogin(SecurityUtils.getCurrentLogin());
            userDetail.setBuildingId(insertedId);
            userDetail.setManagement(true);
            userDetailRepository.create(userDetail);
            userBuildingRepository.create(userDetail);

        }
        json.setResponse(insertedId);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

}
