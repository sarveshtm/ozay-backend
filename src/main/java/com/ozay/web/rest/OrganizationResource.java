package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Role;
import com.ozay.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

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
