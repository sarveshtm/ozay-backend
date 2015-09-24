package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.model.InvitedMember;
import com.ozay.model.Member;
import com.ozay.model.OrganizationUserActivationKey;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.service.InvitedMemberService;
import com.ozay.service.MailService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.FieldErrorDTO;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private MailService mailService;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private InvitedMemberRepository invitedMemberRepository;

    @Inject
    private InvitedMemberService invitedMemberService;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private BuildingRepository buildingRepository;

    @Inject
    private OrganizationUserActivationKeyRepository organizationUserActivationKeyRepository;




    /**
     * POST  /rest/register -> register the user.
     */
    @RequestMapping(value = "/rest/register",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed

    public ResponseEntity<?> registerAccount(@RequestBody UserDTO userDTO, HttpServletRequest request,
                                             HttpServletResponse response) {
        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(userDTO.getEmail())
                    .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                    .orElseGet(() -> {
                        User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
                            userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
                            userDTO.getLangKey());
                        final Locale locale = Locale.forLanguageTag(user.getLangKey());
                        String content = createHtmlContentFromTemplate(user, locale, request, response);
                        mailService.sendActivationEmail(user.getEmail(), content, locale);
                        return new ResponseEntity<>(HttpStatus.CREATED);
                    })
            );
    }


    /**
     * GET  /rest/activate -> activate the registered user.
     */
    @RequestMapping(value = "/rest/activate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {

        // check if this user is invited user
        if(accountRepository.isInvitedUser(key) == true){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return Optional.ofNullable(userService.activateRegistration(key))
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    //    /**
//     * POST  /rest/invitation -> invite member register the user.
//     */
    @RequestMapping(value = "/rest/account/invitation",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> sendInvitationToMember(@RequestBody Member member, HttpServletRequest request,
                                                    HttpServletResponse response) {
        if(member.getUserId() != 0 || member.getUserId() != null){
            new ResponseEntity<>("User is already exist", HttpStatus.BAD_REQUEST);
        }
        // Email is username
        member.setLogin(member.getEmail());
        return userRepository.findOneByEmail(member.getEmail())
            .map(user -> {
                member.setUserId(user.getId());
                memberRepository.update(member);
                Building building = buildingRepository.getBuilding(member.getBuildingId());

                String baseUrl = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort();

                mailService.sendInvitedMail(user, building, baseUrl);
                return new ResponseEntity<>(HttpStatus.OK);
            })
            .orElseGet(() -> {
                InvitedMember invitedMember = invitedMemberService.createInvitedUserInformation(member, "en");
                final Locale locale = Locale.forLanguageTag(invitedMember.getLangKey());
                String content = createInvitationFromTemplate(member, invitedMember,  locale, request, response);
                mailService.sendActivationInvitationCompleteEmail(member.getEmail(), content, locale);
                return new ResponseEntity<>(HttpStatus.CREATED);});
    }


    /**
     * GET  organization-user
     */
    @RequestMapping(value = "/rest/activate-invited-user",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getOrganizationUserByKey(@RequestParam(value = "key") String key) {
        log.debug("REST request to get Organization User : key {} ", key);
        OrganizationUserActivationKey organizationUserActivationKey = organizationUserActivationKeyRepository.findByKey(key);

        User user = userRepository.findOneById(organizationUserActivationKey.getUserId()).get();

        if(user == null || user.getActivated() == true){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }


    /**
     * POST  /rest/activate-invited-user -> activate invited organization user.
     */
    @RequestMapping(value = "/rest/activate-invited-user",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> activateOrganizationUser(@RequestParam(value = "key") String key, @RequestBody UserDTO userDTO,  HttpServletRequest request,
                                                      HttpServletResponse response) {

        if(key == null){
            return new ResponseEntity<>("Key is not set", HttpStatus.BAD_REQUEST);
        }

        OrganizationUserActivationKey organizationUserActivationKey = organizationUserActivationKeyRepository.findByKey(key);
        if(organizationUserActivationKey == null){
            return new ResponseEntity<>("Key doesn't exist", HttpStatus.BAD_REQUEST);
        }

        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> {
                return new ResponseEntity<String>("login already in use", HttpStatus.BAD_REQUEST);
            })
            .orElseGet(() -> userRepository.findOneById(organizationUserActivationKey.getUserId())
                    .map(user -> {
                        user.setId(organizationUserActivationKey.getUserId());
                        user.setLogin(userDTO.getLogin());
                        user.setPassword(userDTO.getPassword());
                        user.setFirstName(userDTO.getFirstName());
                        user.setLangKey(userDTO.getLastName());
                        user.setActivated(true);
                        user = userService.activateInvitedUser(user);
                        organizationUserActivationKeyRepository.updateUsed(user.getId());

                        final Locale locale = Locale.forLanguageTag(user.getLangKey());
                        String content = createInvitedUserRegisteredTemplate(user, locale, request, response);
                        mailService.sendActivationInvitationEmail(user.getEmail(), content, locale);
                        return new ResponseEntity<>("Success", HttpStatus.OK);

                    })
                    .orElseGet(() -> {
                        return new ResponseEntity<>("User already activated", HttpStatus.BAD_REQUEST);
                    })
            );


//                return userRepository.findOneByLogin(userDTO.getLogin())
//                    .map(user -> {
//                        JsonResponse jsonResponse = new JsonResponse();
//                        jsonResponse.setSuccess(false);
//                        FieldErrorDTO fieldErrorDTO = new FieldErrorDTO();
//                        fieldErrorDTO.setField("login");
//                        fieldErrorDTO.setMessage("login already in use");
//                        jsonResponse.addFieldErrorDTO(fieldErrorDTO);
//                        return new ResponseEntity<JsonResponse>(jsonResponse, HttpStatus.BAD_REQUEST);
//
//                    })
//                    .orElse(() -> userRepository.findOneById(organizationUserActivationKey.getId())
//                            .map(user -> {
//                                user.setLogin(userDTO.getLogin());
//                                user.setPassword(userDTO.getPassword());
//                                user.setFirstName(userDTO.getFirstName());
//                                user.setLangKey(userDTO.getLastName());
//                                user.setActivated(true);
//                                userRepository.save(user);
//                                organizationUserActivationKeyRepository.updateUsed(user.getId());
//
//                                final Locale locale = Locale.forLanguageTag(user.getLangKey());
//                                String content = createInvitedUserRegisteredTemplate(user, locale, request, response);
//                                mailService.sendActivationInvitationEmail(user.getEmail(), content, locale);
//                                return new ResponseEntity<>(HttpStatus.OK);
//                            })
//                            .orElseGet(() -> {
//
//                                JsonResponse jsonResponse = new JsonResponse();
//                                jsonResponse.setSuccess(false);
//                                FieldErrorDTO fieldErrorDTO = new FieldErrorDTO();
//                                fieldErrorDTO.setField("login");
//                                fieldErrorDTO.setMessage("User already activated");
//                                jsonResponse.addFieldErrorDTO(fieldErrorDTO);
//                                return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
//
//                            })
//                    );


    }

    /**
     * GET  /rest/invitation/activate -> activate the invited member.
     */
    @RequestMapping(value = "/rest/invitation-activate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activateInvitedMember(@RequestParam(value = "key") String key) {
        return Optional.ofNullable(invitedMemberService.getDataByKey(key))
            .map(user -> new ResponseEntity<String>(HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }


    /**
     * POST  /rest/register -> register the member.
     */
    @RequestMapping(value = "/rest/invitation-activate",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<?> registerInvitedMember(@RequestBody UserDTO userDTO, @RequestParam(value = "key") String key, HttpServletRequest request,
                                                   HttpServletResponse response) {

        return userRepository.findOneByLogin(userDTO.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> {
                if (key == null) {
                    return new ResponseEntity<>("Key is not set", HttpStatus.BAD_REQUEST);
                }
                InvitedMember invitedMember = invitedMemberService.getDataByKey(key);
                Member member = memberRepository.findOne(invitedMember.getMemberId());
                log.debug("User detail info {}", member);
                userDTO.setEmail(member.getEmail());
                userDTO.setFirstName(member.getFirstName());
                userDTO.setLastName(member.getLastName());
                if (userRepository.findOneByEmail(userDTO.getEmail()) != null) {
                    //return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
                }

                log.debug("Validation fine for registerInvited User");

                User user = userService.createUserInformation(userDTO.getLogin(), userDTO.getPassword(),
                    userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail().toLowerCase(),
                    userDTO.getLangKey());

                user.setActivated(true);
                userRepository.save(user);

                member.setUserId(user.getId());
                member.setLogin(user.getLogin());
                memberRepository.update(member);


                invitedMember.setActivated(true);
                invitedMemberRepository.activateInvitedMember(invitedMember);

                final Locale locale = Locale.forLanguageTag(user.getLangKey());
                String content = createInvitedUserRegisteredTemplate(user, locale, request, response);
                mailService.sendActivationInvitationEmail(user.getEmail(), content, locale);
                return new ResponseEntity<>(HttpStatus.CREATED);
            });
    }



    /**
     * GET  /rest/authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/rest/authenticate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/rest/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(
                new UserDTO(
                    user.getId(),
                    user.getLogin(),
                    null,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getLangKey(),
                    user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList())),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/rest/account/building/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserDTO> getAccountWithBuildingId(@PathVariable Long buildingId, @RequestParam(value = "organization") Long organizationId ) {

        if(organizationId == 0){
            organizationId = null;
        }
        return Optional.ofNullable(userService.getUserWithAuthorities(buildingId, organizationId))
            .map(user -> new ResponseEntity<>(
                new UserDTO(
                    user.getId(),
                    user.getLogin(),
                    null,
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getLangKey(),
                    user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList())),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /rest/account -> update the current user information.
     */
    @RequestMapping(value = "/rest/account",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> saveAccount(@RequestBody UserDTO userDTO) {
        User userHavingThisEmail = userRepository.findOneByLogin(userDTO.getEmail()).get();
        if (userHavingThisEmail != null && !userHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return new ResponseEntity<String>("e-mail address already in use", HttpStatus.BAD_REQUEST);
        }
        userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST  /rest/change_password -> changes the current user's password
     */
    @RequestMapping(value = "/rest/account/change_password",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (StringUtils.isEmpty(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/rest/account/sessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> new ResponseEntity<>(
                persistentTokenRepository.findByUser(user),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * DELETE  /rest/account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     */
    @RequestMapping(value = "/rest/account/sessions/{series}",
        method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        if (persistentTokenRepository.findByUser(user).stream()
            .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
            .count() > 0) {

            persistentTokenRepository.delete(decodedSeries);
        }
    }

    private String createHtmlContentFromTemplate(final User user, final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("activationEmail", context);
    }

    private String createInvitationFromTemplate(final Member member, final InvitedMember invitedMember,  final Locale locale, final HttpServletRequest request,
                                                final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("member", member);
        variables.put("invitedUser", invitedMember);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("invitationEmail", context);
    }

    private String createInvitedUserFromTemplate(final Member member, final InvitedMember invitedMember,  final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("member", member);
        variables.put("invitedUser", invitedMember);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("invitationEmail", context);
    }

    private String createInvitedUserRegisteredTemplate(final User user, final Locale locale, final HttpServletRequest request,
                                                       final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("invitedMemberActivationEmail", context);
    }

    @RequestMapping(value = "/rest/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {

        return userService.requestPasswordReset(mail)
            .map(user -> {
                String baseUrl = request.getScheme() +
                    "://" +
                    request.getServerName() +
                    ":" +
                    request.getServerPort();
                mailService.sendPasswordResetMail(user, baseUrl);
                return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
            }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/rest/account/reset_password/finish",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestParam(value = "key") String key, @RequestParam(value = "newPassword") String newPassword) {
        if (!checkPasswordLength(newPassword)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(newPassword, key)
            .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) && password.length() >= UserDTO.PASSWORD_MIN_LENGTH && password.length() <= UserDTO.PASSWORD_MAX_LENGTH);
    }
}
