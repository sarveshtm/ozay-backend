package com.ozay.web.rest.dto;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by naofumiezaki on 3/8/15.
 * Test
 */
public class NotificationDTO {
    private int buildingId;
    private DateTime issueDate;
    private String subject;
    private String notice;
    private boolean management;
    private boolean staff;
    private boolean board;
    private boolean resident;
    private boolean individual;
    private List<Integer> individuals;
    public NotificationDTO(){}
    public NotificationDTO(int buildingId, DateTime issueDate, String subject, String notice, boolean management, boolean staff, boolean board, boolean resident, boolean individual, List<Integer> individuals) {
        this.buildingId = buildingId;
        this.issueDate = issueDate;
        this.subject = subject;
        this.notice = notice;
        this.management = management;
        this.staff = staff;
        this.board = board;
        this.resident = resident;
        this.individual = individual;
        this.individuals = individuals;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public DateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public List<Integer> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Integer> individuals) {
        this.individuals = individuals;
    }

    public String toString() {
        return "NotifiactionDTO{" +

            ", individualSize='" + this.individuals.size() + '\'' +
            "}";
    }
}
