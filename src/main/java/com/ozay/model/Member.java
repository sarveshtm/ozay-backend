package com.ozay.model;

import org.joda.time.DateTime;

import java.util.Set;

public class Member {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String login;
    private Long buildingId;
    private Long userId;
    private Double ownership;
    private String parking;
    private String unit;
    private DateTime expirationDate;
    private boolean deleted;

    private Set<Role> roles;

    // Special email
    private String userEmail;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public Double getOwnership() {
        return ownership;
    }

    public void setOwnership(Double ownership) {
        this.ownership = ownership;
    }


    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id='" + id + '\'' +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", user_id='" + userId + '\'' +
            "}";
    }
}
