package com.ozay.service;

import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.model.Account;
import com.ozay.model.Member;
import com.ozay.model.Subscription;
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
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

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
    private SubscriptionRepository subscriptionRepository;

    public User activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);

        return Optional.ofNullable(userRepository.getUserByActivationKey(key))
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                // TODO remove this in the future
                Subscription subscription = new Subscription();
                subscription.setUserId(user.getId());
                subscriptionRepository.create(subscription);
                return user;
            })
            .orElse(null);
    }

    @Transactional
    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        User currentLoginUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        newUser.setLogin(login);

        newUser.setCreatedBy(SecurityUtils.getCurrentLogin());


        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        if(newUser.getCreatedBy() == null){
            newUser.setCreatedBy("System");
        }

        accountRepository.insertUser(newUser);
        for(Authority authority1 : newUser.getAuthorities()){
            accountRepository.insertAuthority(authority1, newUser.getLogin());
        }
        User createdUser = userRepository.findOneByLogin(newUser.getLogin());
//        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return createdUser;
    }


    public void updateUserInformation(String firstName, String lastName, String email) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        userRepository.save(currentUser);
        log.debug("Changed Information for User: {}", currentUser);
    }

    public void changePassword(String password) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        String encryptedPassword = passwordEncoder.encode(password);
        currentUser.setPassword(encryptedPassword);
        userRepository.save(currentUser);
        log.debug("Changed password for User: {}", currentUser);
    }

    private Account getUserInformation(User user, Long buildingId){
        Account account = accountRepository.getLoginUserInformation(user, buildingId);

        log.debug("Let's check Account: {}", account);
        if(account != null && user.getId() == account.getSubscriberId()){
            log.debug("This is subscriber then: {}", account);
            user.getAuthorities().add(new Authority("ROLE_SUBSCRIBER"));
            user.getAuthorities().add(new Authority("ROLE_MANAGEMENT"));
        }
        return account;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        currentUser.getAuthorities().size(); // eagerly load the association

        Account account = this.getUserInformation(currentUser, null);

        return currentUser;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long buildingId) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        currentUser.getAuthorities().size(); // eagerly load the association

        boolean isAdmin = false;
        for(Authority authority : currentUser.getAuthorities()){
            if(authority.getName().equals("ROLE_ADMIN")){
                isAdmin = true;
                break;
            }
        }
        if(isAdmin == false) {
            Account account = this.getUserInformation(currentUser, buildingId);
            try {
                Member member = memberRepository.getMemberDetailByBuildingAndUserId(currentUser.getId(), buildingId);

                if (member.isManagement() == true) {
                    currentUser.getAuthorities().add(new Authority("ACCESS_DIRECTORY"));
                    currentUser.getAuthorities().add(new Authority("ACCESS_NOTIFICATION"));
                } else if (member.isStaff() == true || member.isBoard()) {
                    currentUser.getAuthorities().add(new Authority("ACCESS_NOTIFICATION"));
                }
            } catch (Exception e) {
                log.debug("UserService getUserWithAuthoritiesAddMoreAuthrities no user detail found {}", currentUser.getLogin());
            }
        }

        return currentUser;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        List<PersistentToken> tokens = persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1));
        for (PersistentToken token : tokens) {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        }
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = userRepository.findNotActivatedUsersByCreationDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}
