package com.ozay.web.rest.dto;

import java.util.List;

public class OrganizationUserDTO {

    private Long organizationId;

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private boolean activated;

    private List<String> roles;

    public OrganizationUserDTO() {
    }

    public OrganizationUserDTO(Long organizationId, Long userId, String firstName, String lastName, String email, String langKey,
                               List<String> roles) {
        this.organizationId = organizationId;
        this.userId=userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrganizationUserDTO{");
        sb.append("organizationId='").append(organizationId).append('\'');
        sb.append(", userId='").append(userId).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }
}
