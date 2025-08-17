package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "quotes")
@EqualsAndHashCode(callSuper = true)
public class Quote extends BaseEntity {

    @Column(name = "quote_number", nullable = false)
    private String quoteNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "subtotal", precision = 19, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 5, scale = 4)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total", precision = 19, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuoteStatus status = QuoteStatus.DRAFT;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Column(name = "sent_date")
    private LocalDateTime sentDate;

    @Column(name = "customer_response_date")
    private LocalDateTime customerResponseDate;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "approval_token")
    private String approvalToken;

    @Column(name = "approval_token_expires")
    private LocalDateTime approvalTokenExpires;

    @Column(name = "approval_required")
    private Boolean approvalRequired = true;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    public enum QuoteStatus {
        DRAFT, PENDING_APPROVAL, SENT, VIEWED, APPROVED, REJECTED, EXPIRED, CONVERTED
    }

    public void calculateTotal() {
        BigDecimal itemsTotal = items.stream()
            .map(item -> item.getQuantity().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.subtotal = itemsTotal;
        this.taxAmount = this.subtotal.multiply(this.taxRate);
        this.total = this.subtotal.add(this.taxAmount).subtract(this.discountAmount);
    }
}

