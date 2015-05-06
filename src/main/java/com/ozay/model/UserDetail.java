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

    private Double ownership;
    private boolean renter;
    private String parking;
    private String unit;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    private DateTime expirationDate;

    private boolean management;
    private boolean staff;

    private boolean board;
    private boolean resident;

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

    public Double getOwnership() {
        return ownership;
    }

    public void setOwnership(Double ownership) {
        this.ownership = ownership;
    }

    public boolean isRenter() {
        return renter;
    }

    public void setRenter(boolean renter) {
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

    public boolean isManagement() {
        return management;
    }

    public void setManagement(boolean management) {
        this.management = management;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public boolean isBoard() {
        return board;
    }

    public void setBoard(boolean board) {
        this.board = board;
    }

    public boolean isResident() {
        return resident;
    }

    public void setResident(boolean resident) {
        this.resident = resident;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
