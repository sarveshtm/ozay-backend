package com.ozay.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ozay.model.Member;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.UserRepository;
import com.ozay.service.MemberService;
import com.ozay.service.UserService;
import com.ozay.web.rest.dto.FieldErrorDTO;
import com.ozay.web.rest.dto.JsonResponse;
import com.ozay.web.rest.dto.MemberListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MemberService memberService;


    /**
     * GET  /rest/member/:login -> get the "Building" ID
     */
    @RequestMapping(value = "/member/building/{buildingId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Member> getAll(@PathVariable int buildingId) {
        log.debug("REST request to get all building members");
        return memberRepository.getAllUsersByBuilding(buildingId);
    }

    /**
     * GET  /rest/member/building/{buildingId}/{id} -> get the "Building" by bu
     */
    @RequestMapping(value = "/member/building/{buildingId}/member/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Member> getMemberDetail(@PathVariable int buildingId, @PathVariable Long id) {
        log.debug("REST request to get Building ID : {}", buildingId);
        log.debug("REST request to get Building login: {}", id);
        return Optional.ofNullable(memberRepository.findOne(id))
            .map(member -> new ResponseEntity<>(member, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * GET  "/member/building_user_count/{buildingId}", -> get number of members in the building
     */
    @RequestMapping(value = "/member/building/{buildingId}/building_user_count",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> getNumberOfResidents(@PathVariable int buildingId) {
        JsonResponse jsonResponse = new JsonResponse();
        log.debug("REST request to get all User Details");
        Integer num = memberRepository.getAllUsersByBuilding(buildingId).size();
        jsonResponse.setResponse(num.toString());
        return new ResponseEntity<JsonResponse>(jsonResponse,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * POST  /member -> Create a new member.
     */
    @RequestMapping(value = "/member/building/{buildingId}",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> create(@RequestBody Member member) {
        JsonResponse json = new JsonResponse();

        if(member.getBuildingId() == null || member.getBuildingId() == 0){
            log.error("REST request : user detail create bad request {} ", member);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if(member.getUnit() != null){
            member.setUnit(member.getUnit().toUpperCase());
        }

        if(this.checkIfUserAlreadyExistInUnit(member) == true){
            log.debug("User already exists");
            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO("Email", "Already exists");
            json.setSuccess(false);
            json.addFieldErrorDTO(fieldErrorDTO);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        log.debug("REST request :create function : {}", member);

        // get only username before @ (email)
        memberService.create(member);
        log.debug("User Detail create success");
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    private boolean checkIfUserAlreadyExistInUnit(Member member){
        List<Member> members = memberRepository.getUserByBuildingEmailUnit(member.getBuildingId(), member.getEmail(), member.getUnit());
        if(members.size() > 0){
            if(member.getId() != null || member.getId() != 0 ){
                for(Member member1 : members){
                    if(member1.getId() == member.getId() || member1.getId().equals(member.getId())){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;

    }

    /**
     * PUT  /collaborates -> Updates an existing member.
     */
    @RequestMapping(value = "/member/building/{buildingId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> update(@RequestBody Member member) throws URISyntaxException {
        log.debug("REST request :update  record : {}", member);

        JsonResponse json = new JsonResponse();
        if(member.getId() == null || member.getBuildingId() == null || member.getBuildingId() == 0) {
            log.error("REST request : user detail update bad request {} ", member);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if(this.checkIfUserAlreadyExistInUnit(member) == true){
            log.debug("User already exists");
            FieldErrorDTO fieldErrorDTO = new FieldErrorDTO("Email", "Already exists");
            json.setSuccess(false);
            json.addFieldErrorDTO(fieldErrorDTO);
            return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        memberService.update(member);

        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * POST  /member -> delete members.
     */
    @RequestMapping(value = "/member/delete",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<JsonResponse> deleteMembers(@RequestBody List<Member> members) throws URISyntaxException {
        log.debug("REST request :delete  record : {}", members);
        JsonResponse json = new JsonResponse();

        for(Member member : members){
            if(member.isDeleted() == true){
                log.debug("REST request :delete  record : {}", member);
                memberRepository.update(member);
            }
        }
        return new ResponseEntity<JsonResponse>(json,  new HttpHeaders(), HttpStatus.OK);
    }
}
