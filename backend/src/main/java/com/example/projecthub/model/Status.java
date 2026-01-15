package com.example.projecthub.model;

import java.util.EnumSet;
import java.util.Set;

public enum Status {

    PENDING,
    ACTIVE,
    SUSPENDED,
    INACTIVE,
    BANNED;

    private Set<Status> allowedNextStatuses;

    // 🔹 Static block initializes transitions AFTER enum is created
    static {
        PENDING.allowedNextStatuses = EnumSet.of(ACTIVE);
        ACTIVE.allowedNextStatuses = EnumSet.of(SUSPENDED);
        SUSPENDED.allowedNextStatuses = EnumSet.of(ACTIVE);

        INACTIVE.allowedNextStatuses = EnumSet.noneOf(Status.class);
        BANNED.allowedNextStatuses = EnumSet.noneOf(Status.class);
    }

    public boolean canTransitionTo(Status nextStatus) {
        return allowedNextStatuses.contains(nextStatus);
    }
}
