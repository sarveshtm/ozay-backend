package com.ozay.web.rest.dto;

import com.ozay.model.UserDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naofumiezaki on 5/8/15.
 */
public class UserDetailListDTO {
    private String roleName;
    private List<UserDetail> userDetailList;

    public UserDetailListDTO(){}

    public UserDetailListDTO(String roleName) {
        this.roleName = roleName;
        this.userDetailList = new ArrayList<UserDetail>();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<UserDetail> getUserDetailList() {
        return userDetailList;
    }

    public void setUserDetailList(List<UserDetail> userDetailList) {
        this.userDetailList = userDetailList;
    }

    public void addUserDetailToList(UserDetail userDetail){
        this.userDetailList.add(userDetail);
    }
}
