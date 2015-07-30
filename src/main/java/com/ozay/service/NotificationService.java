package com.ozay.service;

import com.ozay.model.Notification;
import com.ozay.domain.User;
import com.ozay.model.Member;
import com.ozay.model.NotificationRecord;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.NotificationDTO;
import com.sendgrid.SendGridException;
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

    @Inject
    private NotificationRecordRepository notificationRecordRepository;

    public int sendNotice(NotificationDTO notificationDto, String baseUrl){
        Notification notification = new Notification();
        notification.setBuildingId(notificationDto.getBuildingId());
        notification.setNotice(notificationDto.getNotice());
        notification.setIssueDate(notificationDto.getIssueDate());

        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();

        notification.setCreatedBy(currentUser.getLogin());

        String buildingName = buildingRepository.getBuilding(notification.getBuildingId()).getName();
        String subject = buildingName + " Notice : " + notificationDto.getSubject();

        notification.setSubject(notificationDto.getSubject());

        List<Member>members = memberRepository.getUserEmailsForNotification(notificationDto);
        log.debug("Notification : size of user details {}", members.size());

        List<NotificationRecord> notificationRecords = new ArrayList<NotificationRecord>();

        for (Member member : members){
            NotificationRecord notificationRecord = new NotificationRecord();
            notificationRecord.setNotificationId(notification.getId());
            notificationRecord.setMemberId(member.getId());
            notificationRecord.setEmail(member.getEmail());
            notificationRecords.add(notificationRecord);
        }

        int emailCount = mailService.sendGrid(notification, notificationRecords, baseUrl);
        if(emailCount == 0){
            return 0;
        }
        Long newId =  notificationRepository.create(notification);

        for(NotificationRecord notificationRecord : notificationRecords){
            notificationRecord.setNotificationId(newId);
            notificationRecordRepository.create(notificationRecord);
        }

        log.debug("REST request to save Notification : {}", notification);

        JsonResponse json = new JsonResponse();

        String message = "Notice is successfully scheduled to " + emailCount + " recipients";
        json.setResponse(message);

        return emailCount;
    }

}
