package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bookings")
@EqualsAndHashCode(callSuper = true)
public class Booking extends BaseEntity {

    @Column(name = "booking_number", nullable = false)
    private String bookingNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id")
    private Quote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.SCHEDULED;

    @Column(name = "assigned_staff_id")
    private String assignedStaffId;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "deposit_amount", precision = 19, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "balance_amount", precision = 19, scale = 2)
    private BigDecimal balanceAmount;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "reminder_sent_48h")
    private Boolean reminderSent48h = false;

    @Column(name = "reminder_sent_24h")
    private Boolean reminderSent24h = false;

    @Column(name = "reminder_sent_1h")
    private Boolean reminderSent1h = false;

    @Column(name = "approval_required")
    private Boolean approvalRequired = true;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public enum BookingStatus {
        PENDING_APPROVAL, SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW, REJECTED
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(scheduledDate) && 
               status != BookingStatus.COMPLETED && 
               status != BookingStatus.CANCELLED;
    }

    public boolean isUpcoming() {
        return scheduledDate.isAfter(LocalDateTime.now()) && 
               (status == BookingStatus.SCHEDULED || status == BookingStatus.CONFIRMED);
    }
}

