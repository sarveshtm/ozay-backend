package com.ozay.service;

import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.model.InvitedUser;
import com.ozay.model.UserDetail;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class InvitedUserService {

    private final Logger log = LoggerFactory.getLogger(InvitedUserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserDetailRepository userDetailRepository;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private InvitedUserRepository invitedUserRepository;

    @Inject
    private UserBuildingRepository userBuildingRepository;



    public InvitedUser getDataByKey(String key) {
        log.debug("Get invited user by key {}", key);
        return invitedUserRepository.getOne(key);
    }


    @Transactional
    public InvitedUser createInvitedUserInformation(UserDetail userDetail, String langKey) {
        User currentLoginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        InvitedUser invitedUser = new InvitedUser();

        invitedUser.setCreatedBy(currentLoginUser.getLogin());
        invitedUser.setUserDetailId(userDetail.getId());
        invitedUser.setLangKey(langKey);
        invitedUser.setActivationKey(RandomUtil.generateActivationKey());

        invitedUserRepository.create(invitedUser);
        log.debug("Created Information for Invited User: {}", invitedUser);
        return invitedUser;
    }

}
