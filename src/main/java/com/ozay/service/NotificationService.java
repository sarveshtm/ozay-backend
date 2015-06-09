package com.ozay.service;

import com.ozay.domain.Notification;
import com.ozay.domain.User;
import com.ozay.model.Member;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.NotificationRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.NotificationDTO;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private MemberRepository memberRepository;

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

        List<Member>members = memberRepository.getUserEmailsForNotification(notificationDto);
        log.debug("Notification : size of user details {}", members.size());
        List<String> emailList = new ArrayList<String>();
        for (Member member : members){
            emailList.add(member.getEmail());
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
