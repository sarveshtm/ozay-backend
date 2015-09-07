package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.AccountInformation;
import com.ozay.model.Building;
import com.ozay.repository.AccountRepository;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.UserRepository;
import com.ozay.security.SecurityUtils;
import com.ozay.service.MemberService;
import com.ozay.service.UserService;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class BuildingResource {

    @Inject
    private MemberRepository memberRepository;
    @Inject
    private BuildingRepository buildingRepository;
    @Inject
    private MemberService memberService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;
    @Inject
    private AccountRepository accountRepository;

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);


    /**
     * GET  /rest/building/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/buildings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Building> getAll(HttpServletRequest request) {
        log.debug("REST request to get building by user");
        log.debug("REST GET LOGIN USER : {}", SecurityUtils.getCurrentLogin());
        User user = userService.getUserWithAuthorities();
        boolean isAdmin = false;
        for(Authority authority : user.getAuthorities()){
            if(authority.getName().equals("ROLE_ADMIN")){
                isAdmin = true;
                break;
            }
        }

        List<Building> buildingList = null;
        if(isAdmin == true){
            log.debug("REST GET get buildings admin");
            buildingList=  buildingRepository.getBuildings();
        } else {
            log.debug("REST GET get buildings getBuildingsByUser");
            buildingList = buildingRepository.getBuildingsUserCanAccess(user);
        }

        return buildingList;
    }

    /**
     * GET  /rest/building/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/buildings/organization/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Building> getAllByOrganization(@PathVariable long id) {
        log.debug("REST request to get building by organization ID");

        return buildingRepository.getBuildingsByOrganization(id);
    }

    /**
     * GET  /rest/building/:id -> get the "Building" ID
     */
    @RequestMapping(value = "/buildings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Building> getOne(@PathVariable long id) {
        log.debug("REST request to get building by id");

        return Optional.ofNullable(buildingRepository.getBuilding(id))
            .map(building -> new ResponseEntity<>(building, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST  /notifications -> Create a new building.
     */
    @RequestMapping(value = "/buildings",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Building building) {
        log.debug("REST request : Building create function");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        building.setCreatedBy(user.getId());
        building.setLastModifiedBy(user.getId());
        AccountInformation accountInformation = accountRepository.getLoginUserInformation(user, null, null);
        building.setOrganizationId(accountInformation.getOrganizationId());
        Integer insertedId = buildingRepository.create(building);
        log.debug("REST request : Building insertedId " + insertedId);
        JsonResponse json = new JsonResponse();
//        if(insertedId > 0){
//            Member member = new Member();
//            member.setUserId(user.getId());
//            member.setFirstName(user.getFirstName());
//            member.setLastName(user.getLastName());
//            member.setBuildingId(insertedId);
//            member.setManagement(true);
//            memberRepository.create(member);
//            userBuildingRepository.create(member);
//        }
        json.setResponse(insertedId);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * PUT  / building -> update building.
     */
    @RequestMapping(value = "/buildings",
        method = RequestMethod.PUT,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> update(@RequestBody Building building) {
        log.debug("REST request : Building update function{}", building);
        buildingRepository.update(building);
        JsonResponse json = new JsonResponse();
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

}
