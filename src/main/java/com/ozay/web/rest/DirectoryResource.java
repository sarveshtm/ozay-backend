package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.web.rest.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
