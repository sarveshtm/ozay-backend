package com.ozay.service;

import com.ozay.model.Member;
import com.ozay.model.Role;
import com.ozay.repository.MemberRepository;
import com.ozay.repository.RoleMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Inject
    private MemberRepository memberRepository;

    @Inject
    private RoleMemberRepository roleMemberRepository;


//    public List<MemberListDTO> createMemberListByRole(List<Member> memberList){
//        List<MemberListDTO> memberListDTOs = new ArrayList<MemberListDTO>();
//
//        MemberListDTO managementList = new MemberListDTO("Management");
//        MemberListDTO staffList = new MemberListDTO("Staff");
//        MemberListDTO boardList = new MemberListDTO("Board");
//        MemberListDTO residentList = new MemberListDTO("Resident");
//
//        for(Member member : memberList){
//            if(member.isManagement() == true){
//                managementList.addMemberToList(member);
//            } else if(member.isStaff()){
//                staffList.addMemberToList(member);
//            } else if(member.isBoard() == true){
//                boardList.addMemberToList(member);
//            } else if(member.isResident()){
//                residentList.addMemberToList(member);
//            }
//        }
//
//        memberListDTOs.add(managementList);
//        memberListDTOs.add(staffList);
//        memberListDTOs.add(boardList);
//        memberListDTOs.add(residentList);
//
//        return memberListDTOs;
//    }

    @Transactional
    public void create(Member member){
        Long id = memberRepository.create(member);

        this.createRoleMemberPermissions(member);
    }

    @Transactional
    public void update(Member member){
        Member currentMember = memberRepository.findOne(member.getId());
        member.setUnit(member.getUnit().toUpperCase());
        memberRepository.update(member);

        roleMemberRepository.deleteAll(currentMember.getId());

        this.createRoleMemberPermissions(member);
    }

    private void createRoleMemberPermissions(Member member){
        for(Role role : member.getRoles()){
            roleMemberRepository.create(role.getId(), member.getId());
        }
    }
}
