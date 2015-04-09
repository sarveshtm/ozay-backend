package com.ozay.web.rest.dto;

import java.util.Date;

/**
 * Created by naofumiezaki on 3/8/15.
 * Test
 */
public class NotificationDTO {
    private int id;
    private Date notifiedDate;
    private String subject;

    public NotificationDTO(){}

    public NotificationDTO(int id, Date notifiedDate, String subject) {
        this.id = id;
        this.notifiedDate = notifiedDate;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getNotifiedDate() {
        return notifiedDate;
    }

    public void setNotifiedDate(Date notifiedDate) {
        this.notifiedDate = notifiedDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
