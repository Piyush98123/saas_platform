package com.saasplatform.service;

import com.saasplatform.entity.Booking;
import com.saasplatform.entity.Quote;
import com.saasplatform.entity.User;
import com.saasplatform.repository.BookingRepository;
import com.saasplatform.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApprovalService {

    private final QuoteRepository quoteRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;
    private final PermissionService permissionService;

    /**
     * Submit quote for approval
     */
    public Quote submitQuoteForApproval(Long quoteId, String tenantId, User submitter) {
        Quote quote = quoteRepository.findByTenantIdAndId(tenantId, quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (!permissionService.canCreateQuotes(submitter)) {
            throw new RuntimeException("User does not have permission to create quotes");
        }

        quote.setStatus(Quote.QuoteStatus.PENDING_APPROVAL);
        quote.setApprovalRequired(true);
        quote.setUpdatedBy(submitter.getEmail());
        quote.setUpdatedAt(LocalDateTime.now());

        Quote savedQuote = quoteRepository.save(quote);

        // Create notification for company admin
        notificationService.createQuoteApprovalNotification(
            savedQuote.getId(), 
            savedQuote.getTitle(), 
            submitter, 
            tenantId
        );

        log.info("Quote {} submitted for approval by user {}", quoteId, submitter.getEmail());
        return savedQuote;
    }

    /**
     * Approve quote
     */
    public Quote approveQuote(Long quoteId, String tenantId, User approver) {
        Quote quote = quoteRepository.findByTenantIdAndId(tenantId, quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (!permissionService.canApproveQuotes(approver)) {
            throw new RuntimeException("User does not have permission to approve quotes");
        }

        quote.setStatus(Quote.QuoteStatus.APPROVED);
        quote.setApprovedBy(approver.getEmail());
        quote.setApprovedAt(LocalDateTime.now());
        quote.setUpdatedBy(approver.getEmail());
        quote.setUpdatedAt(LocalDateTime.now());

        log.info("Quote {} approved by user {}", quoteId, approver.getEmail());
        return quoteRepository.save(quote);
    }

    /**
     * Reject quote
     */
    public Quote rejectQuote(Long quoteId, String tenantId, User approver, String rejectionReason) {
        Quote quote = quoteRepository.findByTenantIdAndId(tenantId, quoteId)
                .orElseThrow(() -> new RuntimeException("Quote not found"));

        if (!permissionService.canApproveQuotes(approver)) {
            throw new RuntimeException("User does not have permission to approve quotes");
        }

        quote.setStatus(Quote.QuoteStatus.REJECTED);
        quote.setRejectionReason(rejectionReason);
        quote.setUpdatedBy(approver.getEmail());
        quote.setUpdatedAt(LocalDateTime.now());

        log.info("Quote {} rejected by user {} with reason: {}", quoteId, approver.getEmail(), rejectionReason);
        return quoteRepository.save(quote);
    }

    /**
     * Submit booking for approval
     */
    public Booking submitBookingForApproval(Long bookingId, String tenantId, User submitter) {
        Booking booking = bookingRepository.findByTenantIdAndId(tenantId, bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!permissionService.canCreateBookings(submitter)) {
            throw new RuntimeException("User does not have permission to create bookings");
        }

        booking.setStatus(Booking.BookingStatus.PENDING_APPROVAL);
        booking.setApprovalRequired(true);
        booking.setUpdatedBy(submitter.getEmail());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        // Create notification for company admin
        notificationService.createBookingApprovalNotification(
            savedBooking.getId(), 
            savedBooking.getTitle(), 
            submitter, 
            tenantId
        );

        log.info("Booking {} submitted for approval by user {}", bookingId, submitter.getEmail());
        return savedBooking;
    }

    /**
     * Approve booking
     */
    public Booking approveBooking(Long bookingId, String tenantId, User approver) {
        Booking booking = bookingRepository.findByTenantIdAndId(tenantId, bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!permissionService.canApproveBookings(approver)) {
            throw new RuntimeException("User does not have permission to approve bookings");
        }

        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setApprovedBy(approver.getEmail());
        booking.setApprovedAt(LocalDateTime.now());
        booking.setUpdatedBy(approver.getEmail());
        booking.setUpdatedAt(LocalDateTime.now());

        log.info("Booking {} approved by user {}", bookingId, approver.getEmail());
        return bookingRepository.save(booking);
    }

    /**
     * Reject booking
     */
    public Booking rejectBooking(Long bookingId, String tenantId, User approver, String rejectionReason) {
        Booking booking = bookingRepository.findByTenantIdAndId(tenantId, bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!permissionService.canApproveBookings(approver)) {
            throw new RuntimeException("User does not have permission to approve bookings");
        }

        booking.setStatus(Booking.BookingStatus.REJECTED);
        booking.setRejectionReason(rejectionReason);
        booking.setUpdatedBy(approver.getEmail());
        booking.setUpdatedAt(LocalDateTime.now());

        log.info("Booking {} rejected by user {} with reason: {}", bookingId, approver.getEmail(), rejectionReason);
        return bookingRepository.save(booking);
    }

    /**
     * Check if user can approve the given quote
     */
    public boolean canApproveQuote(Quote quote, User user) {
        return permissionService.canApproveQuotes(user) && 
               quote.getStatus() == Quote.QuoteStatus.PENDING_APPROVAL;
    }

    /**
     * Check if user can approve the given booking
     */
    public boolean canApproveBooking(Booking booking, User user) {
        return permissionService.canApproveBookings(user) && 
               booking.getStatus() == Booking.BookingStatus.PENDING_APPROVAL;
    }
}



