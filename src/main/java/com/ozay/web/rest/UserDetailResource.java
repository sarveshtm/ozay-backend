package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Notification;
import com.ozay.domain.User;
import com.ozay.model.UserDetail;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.AuthoritiesConstants;
import com.ozay.web.rest.dto.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserDetailResource {

    private final Logger log = LoggerFactory.getLogger(UserDetailResource.class);

    @Inject
    private UserDetailRepository userDetailRepository;

    /**
     * GET  /rest/userdetails/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/userdetails/building/{buildingId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserDetail> getAll(@PathVariable int buildingId) {
        log.debug("REST request to get all Notifications");
        return userDetailRepository.getAllUsersByBuilding(buildingId);
    }


    /**
     * GET  /rest/userdetails/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/userdetails/building_user_count/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> getNumberOfResidents(@PathVariable int buildingId) {
        JsonResponse jsonResponse = new JsonResponse();
        log.debug("REST request to get all Notifications");
        Integer num = userDetailRepository.getAllUsersByBuilding(buildingId).size();
        jsonResponse.setResponse(num.toString());
        return new ResponseEntity<JsonResponse>(jsonResponse,  new HttpHeaders(), HttpStatus.OK);
    }

}
