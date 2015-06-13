package com.ozay.service;

import com.ozay.domain.User;
import com.ozay.model.InvitedUser;
import com.ozay.model.Member;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.service.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

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
    private MemberRepository memberRepository;

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
    public InvitedUser createInvitedUserInformation(Member member, String langKey) {
        User currentLoginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        InvitedUser invitedUser = new InvitedUser();

        invitedUser.setCreatedBy(currentLoginUser.getLogin());
        invitedUser.setMemberId(member.getId());
        invitedUser.setLangKey(langKey);
        invitedUser.setActivationKey(RandomUtil.generateActivationKey());

        invitedUserRepository.create(invitedUser);
        log.debug("Created Information for Invited User: {}", invitedUser);
        return invitedUser;
    }



}
