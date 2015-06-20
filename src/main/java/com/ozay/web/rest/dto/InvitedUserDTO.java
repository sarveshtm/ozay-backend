package com.ozay.web.rest.dto;

public class InvitedUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private String login;

    private String password;

    private String key;


    public InvitedUserDTO() {
    }

    public InvitedUserDTO(String login, String password, String firstName, String lastName, String email) {
        this.login = login;
        this.password = password;
    }


    public static int getPasswordMinLength() {
        return PASSWORD_MIN_LENGTH;
    }

    public static int getPasswordMaxLength() {
        return PASSWORD_MAX_LENGTH;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InvitedUserDTO{");
        sb.append("login='").append(login).append('\'');
        if(password != null) {
            sb.append(", password='").append(password.length()).append('\'');
        }
        sb.append("keys='").append(key).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
