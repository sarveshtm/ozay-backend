package com.ozay.model;

import org.joda.time.DateTime;

import java.util.Set;

/**
 * Created by naofumiezaki on 6/13/15.
 */
public class Organization {
    private Long id;
    private long userId;
    private String name;
    private String address1;
    private String address2;
    private String phone;
    private String state;
    private String zip;
    private String country;
    private boolean ready;
    private DateTime createdDate;
    private Set<OrganizationAccess> organizationRoles;
    private Set<Role> roles;

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }


    public Set<OrganizationAccess> getOrganizationRoles() {
        return organizationRoles;
    }

    public void setOrganizationRoles(Set<OrganizationAccess> organizationRoles) {
        this.organizationRoles = organizationRoles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
