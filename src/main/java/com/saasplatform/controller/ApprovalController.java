package com.saasplatform.controller;

import com.saasplatform.entity.Booking;
import com.saasplatform.entity.Quote;
import com.saasplatform.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/approvals")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class ApprovalController {

    private final ApprovalService approvalService;

    // Quote Approval Endpoints
    @PostMapping("/quotes/{id}/submit")
    public ResponseEntity<Quote> submitQuoteForApproval(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String submitterEmail) {
        
        try {
            // In a real app, you'd get the user from the security context
            // For now, we'll create a mock user
            com.saasplatform.entity.User submitter = new com.saasplatform.entity.User();
            submitter.setEmail(submitterEmail);
            
            Quote quote = approvalService.submitQuoteForApproval(id, tenantId, submitter);
            return ResponseEntity.ok(quote);
        } catch (Exception e) {
            log.error("Error submitting quote for approval: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/quotes/{id}/approve")
    public ResponseEntity<Quote> approveQuote(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String approverEmail) {
        
        try {
            com.saasplatform.entity.User approver = new com.saasplatform.entity.User();
            approver.setEmail(approverEmail);
            
            Quote quote = approvalService.approveQuote(id, tenantId, approver);
            return ResponseEntity.ok(quote);
        } catch (Exception e) {
            log.error("Error approving quote: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/quotes/{id}/reject")
    public ResponseEntity<Quote> rejectQuote(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String approverEmail,
            @RequestParam String rejectionReason) {
        
        try {
            com.saasplatform.entity.User approver = new com.saasplatform.entity.User();
            approver.setEmail(approverEmail);
            
            Quote quote = approvalService.rejectQuote(id, tenantId, approver, rejectionReason);
            return ResponseEntity.ok(quote);
        } catch (Exception e) {
            log.error("Error rejecting quote: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Booking Approval Endpoints
    @PostMapping("/bookings/{id}/submit")
    public ResponseEntity<Booking> submitBookingForApproval(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String submitterEmail) {
        
        try {
            com.saasplatform.entity.User submitter = new com.saasplatform.entity.User();
            submitter.setEmail(submitterEmail);
            
            Booking booking = approvalService.submitBookingForApproval(id, tenantId, submitter);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            log.error("Error submitting booking for approval: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bookings/{id}/approve")
    public ResponseEntity<Booking> approveBooking(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String approverEmail) {
        
        try {
            com.saasplatform.entity.User approver = new com.saasplatform.entity.User();
            approver.setEmail(approverEmail);
            
            Booking booking = approvalService.approveBooking(id, tenantId, approver);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            log.error("Error approving booking: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bookings/{id}/reject")
    public ResponseEntity<Booking> rejectBooking(
            @PathVariable Long id,
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam String approverEmail,
            @RequestParam String rejectionReason) {
        
        try {
            com.saasplatform.entity.User approver = new com.saasplatform.entity.User();
            approver.setEmail(approverEmail);
            
            Booking booking = approvalService.rejectBooking(id, tenantId, approver, rejectionReason);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            log.error("Error rejecting booking: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Pending Approvals List
    @GetMapping("/pending")
    public ResponseEntity<Object> getPendingApprovals(
            @RequestParam(defaultValue = "default") String tenantId) {
        
        try {
            // This would return a summary of pending approvals
            // For now, return a simple response
            return ResponseEntity.ok(Map.of(
                "message", "Pending approvals endpoint",
                "tenantId", tenantId
            ));
        } catch (Exception e) {
            log.error("Error getting pending approvals for tenant: {}", tenantId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}





