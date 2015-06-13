package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.Account;
import com.ozay.model.Building;
import com.ozay.model.Member;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.MemberService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.BuildingDTO;
import com.ozay.web.rest.dto.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class ManageResource {


    @Inject
    private MemberService memberService;


    private final Logger log = LoggerFactory.getLogger(ManageResource.class);

    /**
     * GET  /rest/building/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/manage/list/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Account> getAll(@PathVariable int id) {
        List<Account> list = new ArrayList<Account>();
        return list;
    }
}
