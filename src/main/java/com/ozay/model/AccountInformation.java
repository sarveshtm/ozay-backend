package com.ozay.model;

/**
 * Created by naofumiezaki on 6/11/15.
 */
public class AccountInformation {
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

    @Override
    public String toString() {
        return "Account{" +
            "subscriberId='" + subscriberId + '\'' +
            "subscriptionId='" + subscriptionId + '\'' +
            "organizationId='" + organizationId + '\'' +
            "}";
    }
}
