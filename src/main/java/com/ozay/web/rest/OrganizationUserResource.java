package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.User;
import com.ozay.model.Member;
import com.ozay.model.AccountInformation;
import com.ozay.model.Organization;
import com.ozay.model.Permission;
import com.ozay.service.*;
import com.ozay.repository.*;
import com.ozay.security.SecurityUtils;
import com.ozay.service.MailService;
import com.ozay.service.MemberService;
import com.ozay.web.rest.dto.FieldErrorDTO;
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
import javax.swing.text.html.Option;
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
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private OrganizationRepository organizationRepository;

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
    public ResponseEntity<User> getOrganizationUser(@PathVariable Long organizationId, @PathVariable Long id) {
        log.debug("REST request to get Organization User : Organization ID {}, User ID {} ", organizationId, id);
        return Optional.ofNullable(organizationUserRepository.findOrganizationUser(organizationId, id)).
            map(user -> new ResponseEntity<>(user, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    /**
     * POST  /organization-user
     */
    @RequestMapping(value = "/organization-user",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> addOrgUser(@RequestBody OrganizationUserDTO organizationUser, HttpServletRequest request,
                                                   HttpServletResponse response) {
        log.debug("REST request to add user to an organization, {}", organizationUser.getEmail());

        if(organizationUser.getUserId() == 0){
            //*** Add User Button ***
            User user = userRepository.findOneByEmail(organizationUser.getEmail()).get();
            if(user !=null){
                //1) Create New User
               user = userService.createUserInformation(organizationUser.getEmail().toLowerCase(),
                   "Initpass",
                   organizationUser.getFirstName(),
                   organizationUser.getLastName(),
                   organizationUser.getEmail().toLowerCase(),
                                                        "en");

                log.debug("User Detail create success");
                organizationUser.setUserId(user.getId());
                final Locale locale = Locale.forLanguageTag(user.getLangKey());
                String content = createHtmlContentFromTemplate(user, locale, request, response);
                mailService.sendActivationEmail(user.getEmail(), content, locale);

            }
            //2) Add to Organization
            organizationUserRepository.create(organizationUser.getOrganizationId(),
                organizationUser.getUserId());
        }
        //3) Role Update


        JsonResponse json = new JsonResponse();
        json.setSuccess(true);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.CREATED);
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

}
