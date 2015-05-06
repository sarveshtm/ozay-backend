package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Notification;
import com.ozay.domain.User;
import com.ozay.model.UserDetail;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.AuthoritiesConstants;
import com.ozay.security.SecurityUtils;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.JsonResponse;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserBuildingRepository userBuildingRepository;


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
    @RequestMapping(value = "/userdetails/building/{buildingId}/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDetail> getUserDetail(@PathVariable int buildingId, @PathVariable String login) {
        log.debug("REST request to get Building ID : {}", buildingId);
        log.debug("REST request to get Building login: {}", login);
        return Optional.ofNullable(userDetailRepository.getAllUserByBuilding(login, buildingId))
            .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        log.debug("REST request to get all User Details");
        Integer num = userDetailRepository.getAllUsersByBuilding(buildingId).size();
        jsonResponse.setResponse(num.toString());
        return new ResponseEntity<JsonResponse>(jsonResponse,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/userdetails",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody UserDetail userDetail) {
        log.debug("REST request :create function");
        if(userDetail.getLogin() == null){
            log.debug("REST request :create new record");
            userService.createUserInformation(userDetail.getUser().getEmail(), "ERT", userDetail.getUser().getFirstName(), userDetail.getUser().getLastName(), userDetail.getUser().getEmail(), "en");
            User user = userRepository.findOneByEmail(userDetail.getUser().getEmail());
            userDetail.setLogin(user.getLogin());
            userDetail.setBuildingId(1);
            userDetailRepository.create(userDetail);
            userBuildingRepository.create(userDetail);
        } else {
            log.debug("REST request :update  record");
            User user = userRepository.findOneByEmail(userDetail.getUser().getEmail());
            user.setFirstName(userDetail.getUser().getFirstName());
            user.setLastName(userDetail.getUser().getLastName());
            user.setEmail(userDetail.getUser().getEmail());
            userRepository.save(user);
            userDetailRepository.update(userDetail);
        }

        JsonResponse json = new JsonResponse();
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

}
