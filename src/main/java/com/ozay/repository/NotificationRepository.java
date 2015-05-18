package com.ozay.repository;

import com.ozay.domain.Notification;
import com.ozay.domain.User;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the Notification entity.
 */
public interface NotificationRepository extends JpaRepository<Notification,Long>{

    @Query("select t from Notification t where t.buildingId = ?1")
    List<Notification> findAllByBuilding(Integer buildingId);

}
