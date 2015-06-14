package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Account;
import com.ozay.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
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
