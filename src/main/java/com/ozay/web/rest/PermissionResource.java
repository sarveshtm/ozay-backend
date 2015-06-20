package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Permission;
import com.ozay.repository.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class PermissionResource {

    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    @Inject
    PermissionRepository permissionRepository;

    /**
     * GET  //permission/{method} -> get Permissions
     * 2 types. Organization or Role
     */
    @RequestMapping(value = "/permission/{method}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Permission>> getOrganizationPermissions(@PathVariable String method) {
        log.debug("REST request to get All permissions type {}", method);
        if(method.equals("organization")){
            return new ResponseEntity<>(permissionRepository.getOrganizationPermissions(), HttpStatus.OK);
        } else if(method.equals("role")) {
            return new ResponseEntity<>(permissionRepository.getRolePermissions(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
