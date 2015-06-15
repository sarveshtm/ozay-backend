package com.ozay.model;

import com.ozay.domain.Authority;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Set;

/**
 * Created by naofumiezaki on 6/11/15.
 */
public class Account {
    private Long subscriberId;
    private Long subscriptionId;
    private Long organizationId;

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Long subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
}
