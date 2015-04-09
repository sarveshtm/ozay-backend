package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Authority;
import com.ozay.domain.PersistentToken;
import com.ozay.domain.User;
import com.ozay.repository.PersistentTokenRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.MailService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;
import com.ozay.web.rest.dto.MemberDTO;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);


    /**
     * GET  /rest/directory/members -> get directory info
     */
    @RequestMapping(value = "/rest/directory/members",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MemberDTO> getmembers() {
        List<MemberDTO> list = new ArrayList<MemberDTO>();
        for (int i = 0; i < 10; i++) {
            list.add(createMember(i));
        }

        return list;
    }

    private MemberDTO createMember(int i) {
        MemberDTO m = new MemberDTO();
        m.setId(1);
        m.setFirstName("Bill");
        m.setLastName("Clinton");
        m.setEmail("test@gmail.com");
        m.setPhone("212-434-2324");
        m.setOwnership(1.34);
        m.setRenter(false);
        m.setUnit("2A");
        if(i<5){
            m.setGroup("Management");
        } else {
            m.setGroup("Resident");
        }
        return m;
    }

}
