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



    public User activateInvitation(String key) {
        log.debug("Activating user for activation key {}", key);
        return Optional.ofNullable(invitedUserRepository.getOne(key))
            .map(invited_user -> {
                User newUser = new User();
                Authority authority = authorityRepository.findOne("ROLE_USER");
                Set<Authority> authorities = new HashSet<>();
                authorities.add(authority);
                newUser.setAuthorities(authorities);
                UserDetail userDetail = userDetailRepository.getOne(invited_user.getUserDetailId());
                newUser.setFirstName(userDetail.getFirstName());
                newUser.setLangKey(invited_user.getLangKey());
                newUser.setActivationKey(invited_user.getActivationKey());
                newUser.setEmail(userDetail.getEmail());
                newUser.setLogin(userDetail.getEmail());
                newUser.setPassword(invited_user.getPassword());
                newUser.setActivated(true);
                newUser.setActivationKey(invited_user.getActivationKey());
                userRepository.save(newUser);
                userDetail.setUserId(newUser.getId());
                userDetailRepository.update(userDetail);
                invited_user.setActivated(true);
                invited_user.setActivatedDate(new DateTime());
                invitedUserRepository.activateInvitedUser(invited_user);
                userBuildingRepository.create(userDetail);
                return newUser;
            })
            .orElse(null);
    }


    @Transactional
    public InvitedUser createInvitedUserInformation(UserDetail userDetail, String password, String langKey) {
        User currentLoginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        InvitedUser invitedUser = new InvitedUser();
        String encryptedPassword = passwordEncoder.encode(password);

        invitedUser.setPassword(encryptedPassword);
        invitedUser.setCreatedBy(currentLoginUser.getLogin());
        invitedUser.setUserDetailId(userDetail.getId());
        invitedUser.setLangKey(langKey);
        invitedUser.setActivationKey(RandomUtil.generateActivationKey());

        invitedUserRepository.create(invitedUser);
        log.debug("Created Information for Invited User: {}", invitedUser);
        return invitedUser;
    }

}
