package com.ozay.web.rest.dto.page;

import com.ozay.model.Member;
import com.ozay.model.Permission;
import com.ozay.model.Role;

import java.util.List;

/**
 * Created by naofumiezaki on 8/30/15.
 */
public class MemberDetailPage {
    private Member member;
    private List<Role> roles;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
