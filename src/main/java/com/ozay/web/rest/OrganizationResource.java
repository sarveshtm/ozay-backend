package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.model.Organization;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Inject
    AccountRepository accountRepository;
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
            .orElse(new ResponseEntity<>( HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/organization",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Organization organization) {
        log.debug("REST request to create an organization, {}", organization);
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        AccountInformation accountInformation = accountRepository.getLoginUserInformation(user, null);
        organization.setSubscriptionId(accountInformation.getSubscriptionId());
        organization.setCreatedBy(user.getId());
        organizationRepository.create(organization);
        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/organization",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> update(@RequestBody Organization organization) {
        log.debug("REST request to update an organization, {}", organization);
        if(organization.getId() == null || organization.getId() == 0 ){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        AccountInformation accountInformation = accountRepository.getLoginUserInformation(user, null);
        organization.setSubscriptionId(accountInformation.getSubscriptionId());
        organization.setModifiedBy(user.getId());
        organizationRepository.update(organization);
        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }


}
