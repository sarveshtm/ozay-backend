package com.ozay.service;

import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
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
    private InvitedMemberRepository invitedMemberRepository;

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


// Used for organization invitation
    public User activateInvitedUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountRepository.updateInvitedOrganizationUser(user);
        return user;
    }

    @Transactional
    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String act_Key = RandomUtil.generateActivationKey();
        //Set password
        if (password.equals("")) password = act_Key + "Ozay";
        String encryptedPassword = passwordEncoder.encode(password);

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
        newUser.setActivationKey(act_Key);
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        if(newUser.getCreatedBy() == null){
            newUser.setCreatedBy("System");
        }

        accountRepository.insertUser(newUser);
        for(Authority authority1 : newUser.getAuthorities()){
            accountRepository.insertAuthority(authority1, newUser.getLogin());
        }
        User createdUser = userRepository.findOneByLogin(newUser.getLogin()).get();
//        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return createdUser;
    }

    @Transactional
    public User createInvitedUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);

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
        User createdUser = userRepository.findOneByLogin(newUser.getLogin()).get();
//        userRepository.save(newUser);
        log.debug("Created Invited User Information for User: {}", newUser);
        return createdUser;
    }


    public void updateUserInformation(String firstName, String lastName, String email) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);
        userRepository.save(currentUser);
        log.debug("Changed Information for User: {}", currentUser);
    }

    public void changePassword(String password) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        String encryptedPassword = passwordEncoder.encode(password);
        currentUser.setPassword(encryptedPassword);
        userRepository.save(currentUser);
        log.debug("Changed password for User: {}", currentUser);
    }

    private AccountInformation getUserInformation(User user, Long buildingId, Long organizationId){

        AccountInformation accountInformation = null;
        if(buildingId == null && organizationId == null){
            accountInformation = accountRepository.getLoginUserInformation(user);
        } else {
            accountInformation = accountRepository.getLoginUserInformation(user, buildingId, organizationId);
        }

        log.debug("Let's check Account: {}", accountInformation);
        if(accountInformation != null && user.getId() == accountInformation.getSubscriberId()){
            log.debug("This is subscriber then: {}", accountInformation);
            user.getAuthorities().add(new Authority("ROLE_SUBSCRIBER"));
            user.getAuthorities().add(new Authority("ROLE_MANAGEMENT"));
        }
        if(accountInformation != null && accountInformation.getAuthorities() != null && accountInformation.getAuthorities().size() > 0 ){
            for(String authority : accountInformation.getAuthorities()){
                if(authority != null){
                    user.getAuthorities().add(new Authority(authority));
                }
            }
        }

        return accountInformation;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        currentUser.getAuthorities().size(); // eagerly load the association

        AccountInformation accountInformation = this.getUserInformation(currentUser, null, null);

        return currentUser;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long buildingId, Long organizationId) {
        User currentUser = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        currentUser.getAuthorities().size(); // eagerly load the association

        boolean isAdmin = false;
        for(Authority authority : currentUser.getAuthorities()){
            if(authority.getName().equals("ROLE_ADMIN")){
                isAdmin = true;
                break;
            }
        }
        if(isAdmin == false) {
            AccountInformation accountInformation = this.getUserInformation(currentUser, buildingId, organizationId);

        }

        return currentUser;
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);

        return userRepository.findOneByResetKey(key)
            .filter(user -> {
                DateTime oneDayAgo = DateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(user -> user.getActivated() == true)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(DateTime.now());
                userRepository.save(user);
                return user;
            });
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
