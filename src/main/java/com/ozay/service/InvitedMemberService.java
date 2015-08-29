package com.ozay.service;

import com.ozay.domain.User;
import com.ozay.model.InvitedMember;
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
public class InvitedMemberService {

    private final Logger log = LoggerFactory.getLogger(InvitedMemberService.class);

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
    private InvitedMemberRepository invitedMemberRepository;


    public InvitedMember getDataByKey(String key) {
        log.debug("Get invited user by key {}", key);
        return invitedMemberRepository.getOne(key);
    }


    @Transactional
    public InvitedMember createInvitedUserInformation(Member member, String langKey) {
        User currentLoginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        InvitedMember invitedMember = new InvitedMember();

        invitedMember.setCreatedBy(currentLoginUser.getLogin());
        invitedMember.setMemberId(member.getId());
        invitedMember.setLangKey(langKey);
        invitedMember.setActivationKey(RandomUtil.generateActivationKey());

        invitedMemberRepository.create(invitedMember);
        log.debug("Created Information for Invited User: {}", invitedMember);
        return invitedMember;
    }



}
