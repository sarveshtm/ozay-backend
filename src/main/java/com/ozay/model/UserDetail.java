package com.ozay.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ozay.domain.User;
import com.ozay.domain.util.CustomDateTimeDeserializer;
import com.ozay.domain.util.CustomDateTimeSerializer;
import org.joda.time.DateTime;

import java.sql.Date;

public class UserDetail {
    private String login;
    private Integer buildingId;
    private String address_1;
    private String address_2;
    private String state;
    private String zip;
    private Double ownership;
    private Boolean renter;
    private String parking;
    private String unit;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime expirationDate;

    private Boolean management;
    private Boolean staff;

    private Boolean board;
    private Boolean resident;

    private User user;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Integer buildingId) {
        this.buildingId = buildingId;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
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

    public Double getOwnership() {
        return ownership;
    }

    public void setOwnership(Double ownership) {
        this.ownership = ownership;
    }

    public Boolean getRenter() {
        return renter;
    }

    public void setRenter(Boolean renter) {
        this.renter = renter;
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

    public Boolean getManagement() {
        return management;
    }

    public void setManagement(Boolean management) {
        this.management = management;
    }

    public Boolean getStaff() {
        return staff;
    }

    public void setStaff(Boolean staff) {
        this.staff = staff;
    }

    public Boolean getBoard() {
        return board;
    }

    public void setBoard(Boolean board) {
        this.board = board;
    }

    public Boolean getResident() {
        return resident;
    }

    public void setResident(Boolean resident) {
        this.resident = resident;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
