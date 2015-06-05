package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.UserDetail;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.service.UserDetailService;
import com.ozay.service.UserService;
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

    @Inject
    private UserDetailService userDetailService;


    /**
     * GET  /rest/userdetails/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/userdetails/building/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserDetailListDTO> getAll(@PathVariable int buildingId) {
        log.debug("REST request to get all Notifications");
        return userDetailService.createUserDetailListByRole(userDetailRepository.getAllUsersByBuilding(buildingId));
    }

    /**
     * GET  /rest/userdetails/building/{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/userdetails/building/{buildingId}/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDetail> getUserDetail(@PathVariable int buildingId, @PathVariable int id) {
        log.debug("REST request to get Building ID : {}", buildingId);
        log.debug("REST request to get Building login: {}", id);
        return Optional.ofNullable(userDetailRepository.getUserByBuilding(id, buildingId))
            .map(userDetail -> new ResponseEntity<>(userDetail, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * GET  "/userdetails/building_user_count/{buildingId}", -> get number of members in the building
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
        JsonResponse json = new JsonResponse();

        if(userDetail.getBuildingId() == null || userDetail.getBuildingId() == 0){
            log.error("REST request : user detail create bad request {} ", userDetail);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        if(userDetail.isManagement() == false && userDetail.isStaff() == false && userDetail.isBoard() == false){
            userDetail.setResident(true);
        }
        userDetail.setUnit(userDetail.getUnit().toUpperCase());

        if(this.checkIfUserAlreadyExistInUnit(userDetail) == true){
            log.debug("User already exists");
            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO("Email", "Already exists");
            json.setSuccess(false);
            json.addFieldErrorDTO(fieldErrorDTO);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        log.debug("REST request :create function : {}", userDetail);

        // get only username before @ (email)
        userDetailRepository.create(userDetail);
        log.debug("User Detail create success");
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    private boolean checkIfUserAlreadyExistInUnit(UserDetail userDetail){
        List<UserDetail> userDetails = userDetailRepository.getUserByBuildingEmailUnit(userDetail.getBuildingId(), userDetail.getEmail(), userDetail.getUnit());
        if(userDetails.size() > 0){
            if(userDetail.getId() != null || userDetail.getId() != 0 ){
                for(UserDetail userDetail1 : userDetails){
                    if(userDetail1.getId() == userDetail.getId() || userDetail1.getId().equals(userDetail.getId())){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;

    }

    /**
     * PUT  /collaborates -> Updates an existing userDetail.
     */
    @RequestMapping(value = "/userdetails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> update(@RequestBody UserDetail userDetail) throws URISyntaxException {
        log.debug("REST request :update  record : {}", userDetail);
        userDetail.setUnit(userDetail.getUnit().toUpperCase());
        JsonResponse json = new JsonResponse();
        if(userDetail.getId() == null || userDetail.getBuildingId() == null || userDetail.getBuildingId() == 0) {
            log.error("REST request : user detail update bad request {} ", userDetail);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if(this.checkIfUserAlreadyExistInUnit(userDetail) == true){
            log.debug("User already exists");
            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO("Email", "Already exists");
            json.setSuccess(false);
            json.addFieldErrorDTO(fieldErrorDTO);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        boolean result = userDetailRepository.update(userDetail);
        log.debug("User Detail update success {}", result);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }
}
