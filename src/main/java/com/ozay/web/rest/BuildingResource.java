package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.domain.Authority;
import com.ozay.domain.User;
import com.ozay.model.Building;
import com.ozay.model.Member;
import com.ozay.repository.BuildingRepository;
import com.ozay.repository.UserBuildingRepository;
import com.ozay.repository.MemberRepository;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    private UserBuildingRepository userBuildingRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserService userService;

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);


    /**
     * GET  /rest/building/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/building",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BuildingDTO> getAll(HttpServletRequest request) {
        log.debug("REST request to get building by user");
        log.debug("REST GET LOGIN USER : {}", SecurityUtils.getCurrentLogin());
        User user = userService.getUserWithAuthorities();
        boolean isAdmin = false;
        for(Authority authority : user.getAuthorities()){
            log.debug(authority.getName());
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
            buildingList = buildingRepository.getBuildingsByUser(user.getId());
        }

        List<BuildingDTO> buildingDtoList = new ArrayList<BuildingDTO>();

        for(Building building : buildingList){
            BuildingDTO buildingDTO = new BuildingDTO();
            buildingDTO.setId(building.getId());
            buildingDTO.setName(building.getName());
            buildingDtoList.add(buildingDTO);

        }
        return buildingDtoList;
    }

    /**
     * POST  /notifications -> Create a new notification.
     */
    @RequestMapping(value = "/building",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Building building) {
        log.debug("REST request : Building create function");
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
        building.setCreatedBy(user.getId());
        building.setLastModifiedBy(user.getId());
        Integer insertedId = buildingRepository.create(building);
        log.debug("REST request : Building insertedId " + insertedId);
        JsonResponse json = new JsonResponse();
        if(insertedId > 0){
            Member member = new Member();
            member.setUserId(user.getId());
            member.setFirstName(user.getFirstName());
            member.setLastName(user.getLastName());
            member.setBuildingId(insertedId);
            member.setManagement(true);
            memberRepository.create(member);
            userBuildingRepository.create(member);
        }
        json.setResponse(insertedId);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

}
