package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.Organization;
import com.ozay.repository.OrganizationRepository;
import com.ozay.repository.RoleRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserRepository userRepository;

    /**
     * GET  /Organization -> get organizations.
     */
    @RequestMapping(value = "/organization",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Organization> getOne() {
        log.debug("REST request to get an organization");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        return Optional.ofNullable(organizationRepository.findOneByUserId(user.getId()))
            .map(organization -> new ResponseEntity<>(organization, HttpStatus.OK))
            .orElse(new ResponseEntity<>(new Organization(), HttpStatus.OK));
    }


}
