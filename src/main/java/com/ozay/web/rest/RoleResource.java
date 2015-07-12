package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Notification;
import com.ozay.model.Role;
import com.ozay.model.RolePermission;
import com.ozay.repository.*;
import com.ozay.service.NotificationService;
import com.ozay.service.RoleService;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    RoleService roleService;

    @Inject
    RoleRepository roleRepository;

    /**
     * GET  /role/{buildingId} -> get all the roles by building.
     */
    @RequestMapping(value = "/role/building/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Role> getAllRolesByBuilding(@PathVariable Long buildingId) {
        log.debug("REST request to get all roles by Building");
        return roleRepository.findAllByBuilding(buildingId);
    }

    /**
     * GET  /role/:id -> get the "id" notification.
     */
    @RequestMapping(value = "/role/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> get(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        return Optional.ofNullable(roleRepository.findOne(id))
            .map(role -> {
                  return  new ResponseEntity<>(
                        role,
                        HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/role",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Role role) {
        log.debug("REST request to save Role : {}", role);
        roleService.create(role);
        JsonResponse json = new JsonResponse();

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/role",
        method = RequestMethod.PUT,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> update(@RequestBody Role role) {
        log.debug("REST request to save Role : {}", role);

        roleService.update(role);
        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/role/delete",
        method = RequestMethod.PUT,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> multiDelete(@RequestBody List<Role> roles) {
        log.debug("REST request to save Role : {}", roles);

        roleService.multiDelete(roles);
        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }


}
