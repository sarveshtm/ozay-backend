package com.ozay.web.rest;

import com.ozay.Application;
import com.ozay.domain.Notification;
import com.ozay.repository.NotificationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotificationResource REST controller.
 *
 * @see NotificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Integer DEFAULT_BUILDING_ID = 0;
    private static final Integer UPDATED_BUILDING_ID = 1;
    private static final String DEFAULT_NOTICE = "SAMPLE_TEXT";
    private static final String UPDATED_NOTICE = "UPDATED_TEXT";

    private static final DateTime DEFAULT_ISSUE_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ISSUE_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ISSUE_DATE_STR = dateTimeFormatter.print(DEFAULT_ISSUE_DATE);
    private static final String DEFAULT_CREATED_BY = "SAMPLE_TEXT";
    private static final String UPDATED_CREATED_BY = "UPDATED_TEXT";

    private static final DateTime DEFAULT_CREATED_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_CREATED_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_CREATED_DATE_STR = dateTimeFormatter.print(DEFAULT_CREATED_DATE);

    @Inject
    private NotificationRepository notificationRepository;

    private MockMvc restNotificationMockMvc;

    private Notification notification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationResource notificationResource = new NotificationResource();
        ReflectionTestUtils.setField(notificationResource, "notificationRepository", notificationRepository);
        this.restNotificationMockMvc = MockMvcBuilders.standaloneSetup(notificationResource).build();
    }

    @Before
    public void initTest() {
        notification = new Notification();
        notification.setBuildingId(DEFAULT_BUILDING_ID);
        notification.setNotice(DEFAULT_NOTICE);
        notification.setIssueDate(DEFAULT_ISSUE_DATE);
        notification.setCreatedBy(DEFAULT_CREATED_BY);
        notification.setCreatedDate(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        // Validate the database is empty
        assertThat(notificationRepository.findAll()).hasSize(0);

        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notification)))
                .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(1);
        Notification testNotification = notifications.iterator().next();
        assertThat(testNotification.getBuildingId()).isEqualTo(DEFAULT_BUILDING_ID);
        assertThat(testNotification.getNotice()).isEqualTo(DEFAULT_NOTICE);
        assertThat(testNotification.getIssueDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testNotification.getCreatedDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notifications
        restNotificationMockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(notification.getId().intValue()))
                .andExpect(jsonPath("$.[0].buildingId").value(DEFAULT_BUILDING_ID))
                .andExpect(jsonPath("$.[0].notice").value(DEFAULT_NOTICE.toString()))
                .andExpect(jsonPath("$.[0].issueDate").value(DEFAULT_ISSUE_DATE_STR))
                .andExpect(jsonPath("$.[0].createdBy").value(DEFAULT_CREATED_BY.toString()))
                .andExpect(jsonPath("$.[0].createdDate").value(DEFAULT_CREATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.buildingId").value(DEFAULT_BUILDING_ID))
            .andExpect(jsonPath("$.notice").value(DEFAULT_NOTICE.toString()))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Update the notification
        notification.setBuildingId(UPDATED_BUILDING_ID);
        notification.setNotice(UPDATED_NOTICE);
        notification.setIssueDate(UPDATED_ISSUE_DATE);
        notification.setCreatedBy(UPDATED_CREATED_BY);
        notification.setCreatedDate(UPDATED_CREATED_DATE);
        restNotificationMockMvc.perform(post("/api/notifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notification)))
                .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(1);
        Notification testNotification = notifications.iterator().next();
        assertThat(testNotification.getBuildingId()).isEqualTo(UPDATED_BUILDING_ID);
        assertThat(testNotification.getNotice()).isEqualTo(UPDATED_NOTICE);
        assertThat(testNotification.getIssueDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testNotification.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testNotification.getCreatedDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(0);
    }
}
