package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.model.Organization;
import com.ozay.model.Permission;
import com.ozay.repository.OrganizationUserRepository;
import com.ozay.repository.PermissionRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class OrganizationUserResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUserResource.class);

    @Inject
    OrganizationUserRepository organizationUserRepository;

    /**
     * GET  /organization-users
     */
    @RequestMapping(value = "/organization-user/{organizationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getOrganizationUsers(@PathVariable Long organizationId) {
        log.debug("REST request to get Organization Users : {}", organizationId);
        return organizationUserRepository.findOrganizationUsers(organizationId);
    }

    /**
     * GET  organization-user
     */
    @RequestMapping(value = "/organization-user/organization/{organizationId}/user/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> getOrganizationUser(@PathVariable Long organizationId, @PathVariable Long userId) {
        log.debug("REST request to get Organization User : Organization ID {}, User ID {} ", organizationId, userId);
        return Optional.ofNullable(organizationUserRepository.findOrganizationUser(organizationId, userId)).
            map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

}
