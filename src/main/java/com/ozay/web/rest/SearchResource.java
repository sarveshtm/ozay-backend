package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.NotificationRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.security.AuthoritiesConstants;
import com.ozay.web.rest.dto.SearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class SearchResource {

    private final Logger log = LoggerFactory.getLogger(SearchResource.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private BuildingRepository buildingRepository;



    /**
     * GET  /notifications -> get all the notifications.
     */
    @RequestMapping(value = "/search/all/{buildingId}/{item}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.ADMIN)
    public List<SearchDTO> getAll(@PathVariable int buildingId, @PathVariable String item) {
        List<SearchDTO> searchDTOs = new ArrayList<SearchDTO>();

        SearchDTO directory = new SearchDTO();
        directory.setType("Directory");
        directory.setList(memberRepository.searchUsers(buildingId, item));
        searchDTOs.add(directory);


        SearchDTO notification = new SearchDTO();
        notification.setType("Notification");

        String likeItem = "%" + item.toLowerCase() + "%";
        notification.setList(notificationRepository.searchNotification(buildingId, likeItem));
        searchDTOs.add(notification);

        return searchDTOs;
    }
}
