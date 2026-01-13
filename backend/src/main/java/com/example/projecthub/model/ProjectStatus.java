package com.example.projecthub.model;

public enum ProjectStatus {
    DRAFT,          // Being prepared by seller
    UNDER_REVIEW,   // Submitted for admin approval
    APPROVED,       // Approved and published
    REJECTED,       // Rejected by admin
    SUSPENDED,      // Temporarily suspended
    ARCHIVED        // No longer available
}
