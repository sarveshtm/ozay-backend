package com.ozay.web.rest.dto;

/**
 * Created by naofumiezaki on 8/30/15.
 */
public class OrganizationUserRoleDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private boolean assigned;

    public OrganizationUserRoleDTO(){}

    public OrganizationUserRoleDTO(Long userId, String firstName, String lastName, boolean assigned) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.assigned = assigned;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
