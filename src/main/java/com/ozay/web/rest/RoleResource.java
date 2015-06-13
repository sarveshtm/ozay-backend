package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Notification;
import com.ozay.model.Role;
import com.ozay.repository.*;
import com.ozay.service.NotificationService;
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
    RoleRepository roleRepository;





    /**
     * GET  /notifications -> get all the notifications.
     */
    @RequestMapping(value = "/role/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Role> getAllByBuilding(@PathVariable Long buildingId) {
        log.debug("REST request to get all roles by Building");
        return roleRepository.findAllByBuilding(buildingId);
    }


}
