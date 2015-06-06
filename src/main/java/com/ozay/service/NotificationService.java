package com.ozay.service;

import com.ozay.domain.Notification;
import com.ozay.domain.User;
import com.ozay.model.UserDetail;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.NotificationRepository;
import com.ozay.repository.UserDetailRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.NotificationDTO;
import com.ozay.web.rest.dto.UserDetailListDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing users.
 */


@Service
@Transactional
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private UserDetailRepository userDetailRepository;

    public int sendNotice(NotificationDTO notificationDto){
        Notification notification = new Notification();
        notification.setBuildingId(notificationDto.getBuildingId());
        notification.setNotice(notificationDto.getNotice());
        notification.setIssueDate(notificationDto.getIssueDate());
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        notification.setCreatedBy(currentUser.getLogin());
        notification.setCreatedDate(new DateTime());
        String buildingName = buildingRepository.getBuilding(notification.getBuildingId()).getName();
        String subject = buildingName + " Notice : " + notificationDto.getSubject();
        notification.setSubject(notificationDto.getSubject());

        List<UserDetail>userDetails = userDetailRepository.getUserEmailsForNotification(notificationDto);
        log.debug("Notification : size of user details {}", userDetails.size());
        List<String> emailList = new ArrayList<String>();
        for (UserDetail userDetail : userDetails){
            emailList.add(userDetail.getEmail());
        }

        int emailCount = mailService.sendGrid(subject, notification.getNotice(), emailList);

        log.debug("REST request to save Notification : {}", notification);
        notificationRepository.save(notification);
        JsonResponse json = new JsonResponse();

        String message = "Notice is successfully scheduled to " + emailCount + " recipients";
        json.setResponse(message);

        return emailCount;
    }



}
