package com.ozay.web.rest.dto;

/**
 * Created by naofumiezaki on 3/8/15.
 * Test
 */
public class MemberDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private double ownership;
    private String email;
    private String unit;
    private boolean renter;
    private String group;

    public MemberDTO(){};

    public MemberDTO(int id, String firstName, String lastName, String phone, double ownership, String email, String unit, boolean renter, String group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.ownership = ownership;
        this.email = email;
        this.unit = unit;
        this.renter = renter;
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getOwnership() {
        return ownership;
    }

    public void setOwnership(double ownership) {
        this.ownership = ownership;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isRenter() {
        return renter;
    }

    public void setRenter(boolean renter) {
        this.renter = renter;
    }
}
