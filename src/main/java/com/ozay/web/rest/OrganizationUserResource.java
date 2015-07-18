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
import javax.servlet.http.HttpServletRequest;
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
    @RequestMapping(value = "/organization-user/{organizationId}/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> getOrganizationUser(@PathVariable Long organizationId, @PathVariable Long id) {
        log.debug("REST request to get Organization User : Organization ID {}, User ID {} ", organizationId, id);
        return Optional.ofNullable(organizationUserRepository.findOrganizationUser(organizationId, id)).
            map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * POST  /organization-user
     */
    @RequestMapping(value = "/organization-user",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> addOrgUser(@RequestBody User organizationUser, HttpServletRequest request) {
        log.debug("REST request to add user to an organization, {}", organizationUser.getEmail());

//        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
//        AccountInformation accountInformation = accountRepository.getLoginUserInformation(user, null);
//        organization.setSubscriptionId(accountInformation.getSubscriptionId());
//        organization.setCreatedBy(user.getId());
//        organizationRepository.create(organization);
        JsonResponse json = new JsonResponse();
        json.setSuccess(true);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

}
