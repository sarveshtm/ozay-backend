package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.Organization;
import com.ozay.model.OrganizationUserActivationKey;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.service.MailService;
import com.ozay.service.OrganizationService;
import com.ozay.service.UserService;
import com.ozay.service.util.RandomUtil;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.OrganizationUserDTO;
import com.ozay.web.rest.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * REST controller for managing Notification.
 */
@RestController
@RequestMapping("/api")
public class OrganizationUserResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationUserResource.class);

    @Inject
    OrganizationUserRepository organizationUserRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private UserService userService;

    @Inject
    private MailService mailService;

    @Inject
    private OrganizationService organizationService;

    @Inject
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private OrganizationRepository organizationRepository;

    @Inject
    private OrganizationUserActivationKeyRepository organizationUserActivationKeyRepository;

    /**
     * GET  /organization-users
     */
    @RequestMapping(value = "/organization-user/{organizationId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getOrganizationUsers(@PathVariable Long organizationId) {
        log.debug("REST request to get Organization Users : {}", organizationId);
        return organizationUserRepository.findOrganizationUsers(organizationId);
    }

    /**
     * GET  organization-user
     */
    @RequestMapping(value = "/organization-user/{organizationId}/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OrganizationUserDTO> getOrganizationUser(@PathVariable Long organizationId, @PathVariable Long id) {
        log.debug("REST request to get Organization User : Organization ID {}, User ID {} ", organizationId, id);
        return Optional.ofNullable(organizationUserRepository.findOrganizationUser(id,organizationId)).
            map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * GET  organization-user
     */
    @RequestMapping(value = "/organization-user/key",
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
     * POST  /organization-user
     */
    @RequestMapping(value = "/organization-user",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> addOrganizationUser(@RequestBody OrganizationUserDTO organizationUser, HttpServletRequest request,
                                                   HttpServletResponse response) {
        log.debug("REST request to add user to an organization, {}", organizationUser.getEmail());

        if(organizationUser.getUserId() == 0) {
            //*** Add User Button ***
            User user = userRepository.findByOneByLoginOrEmail(organizationUser.getEmail());
            if (user == null) {
                //1) Create New User
                user = userService.createUserInformation(
                    organizationUser.getEmail().toLowerCase(),
                    "",
                    organizationUser.getFirstName(),
                    organizationUser.getLastName(),
                    organizationUser.getEmail().toLowerCase(),
                    "en");

                log.debug("User Detail create success");
                organizationUser.setUserId(user.getId());

            } else{
                organizationUser.setUserId(user.getId());
            }
            //2) Add to Organization
            if (organizationUserRepository.findOrganizationUser(organizationUser.getUserId(),
                organizationUser.getOrganizationId())==null){

                organizationUserRepository.create(organizationUser);
            }

        }
        //3) Organization Role Update
       organizationService.updateOrganizationPermission(organizationUser);

        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    /**
     * PUT  /organization-user
     */
    @RequestMapping(value = "/organization-user",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> updateOrganizationUser(@RequestBody OrganizationUserDTO organizationUser) {
        log.debug("REST request to update organization user, {}", organizationUser);
        Optional<User> temp_user = userRepository.findOneById(organizationUser.getUserId());

        if(temp_user == null){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }
        User user = temp_user.get();

        organizationService.updateOrganizationPermission(organizationUser);

        log.debug("REST request user information, {}", user);
        if(user.getActivated() == false){
            user.setFirstName(organizationUser.getFirstName());
            user.setLastName(organizationUser.getLastName());
            user.setEmail(organizationUser.getEmail());
            userRepository.save(user);
        }


        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * POST  /organization-user/invite" -> invite organization user
     */
    @RequestMapping(value = "/organization-user/invite",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> inviteOrganizationUser(@RequestBody OrganizationUserDTO organizationUser,HttpServletRequest request,
                                             HttpServletResponse response) {

        User user = userRepository.findOneById(organizationUser.getUserId()).get();

        if(user == null){
            return new ResponseEntity<>("User doesn't exist", HttpStatus.BAD_REQUEST);
        }
        else if(user.getActivated() == true){
            return new ResponseEntity<>("User already activated", HttpStatus.BAD_REQUEST);
        }
        OrganizationUserActivationKey organizationUserActivationKey = new OrganizationUserActivationKey();

        organizationUserActivationKey.setUserId(organizationUser.getUserId());
        organizationUserActivationKey.setCreatedBy(SecurityUtils.getCurrentLogin());
        organizationUserActivationKey.setActivationKey(RandomUtil.generateActivationKey());

        organizationUserActivationKeyRepository.create(organizationUserActivationKey);

        Organization organization = organizationRepository.findOne(organizationUser.getOrganizationId());

        final Locale locale = Locale.forLanguageTag(user.getLangKey());

        String content = createHtmlContentFromTemplate(user, organization, organizationUserActivationKey, locale, request, response);

        mailService.sendActivationEmail(user.getEmail(), content, locale);

        JsonResponse json = new JsonResponse();
        json.setSuccess(true);
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
    }

    private String createHtmlContentFromTemplate(final User user, final Organization organization, final OrganizationUserActivationKey organizationUserActivationKey, final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", user);
        variables.put("organization", organization);
        variables.put("organizationUserActivationKey", organizationUserActivationKey);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
            request.getServerName() +       // "myhost"
            ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
            locale, variables, applicationContext);
        return templateEngine.process("organizationUserInvitationEmail", context);
    }


}
