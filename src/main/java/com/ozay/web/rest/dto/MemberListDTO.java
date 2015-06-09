package com.ozay.web.rest.dto;

import com.ozay.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 5/8/15.
 */
public class MemberListDTO {
    private String roleName;
    private List<Member> memberList;

    public MemberListDTO(){}

    public MemberListDTO(String roleName) {
        this.roleName = roleName;
        this.memberList = new ArrayList<Member>();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public void addMemberToList(Member member){
        this.memberList.add(member);
    }
}
