package com.ozay.service;

import com.ozay.domain.User;
import com.ozay.model.Member;
import com.ozay.model.Notification;
import com.ozay.model.NotificationRecord;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.web.rest.dto.AccountDTO;
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
public class AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountService.class);


    @Inject
    private MemberRepository memberRepository;


    public AccountDTO getUserInformation(long userId, long buildingId){
        AccountDTO accountDTO = new AccountDTO();



        return accountDTO;
    }
}
