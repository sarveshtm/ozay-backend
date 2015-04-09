package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.security.AuthoritiesConstants;
import com.ozay.web.rest.dto.MemberDTO;
import com.ozay.web.rest.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    /**
     * GET  /rest/directory/members -> get directory info
     */
    @RequestMapping(value = "/rest/notifications/get",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationDTO> getSubjects() {
        List<NotificationDTO> list = new ArrayList<NotificationDTO>();
        for (int i = 0; i < 10; i++) {
            list.add(createNotifications());
        }
        return list;
    }

    /**
     * POST  /rest/users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/rest/notifications/create",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    boolean createNotification() {
        return true;
    }

    private NotificationDTO createNotifications() {
        NotificationDTO m = new NotificationDTO();
        m.setNotifiedDate(new Date());
        m.setSubject("test Subject");

        return m;
    }

}
