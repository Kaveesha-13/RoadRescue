package RoadRescue.controller;

import RoadRescue.model.Booking;
import RoadRescue.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Endpoint for Vehicle Owner to launch a breakdown ticket request
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> data) {
        try {
            Long ownerId = Long.valueOf(data.get("ownerId").toString());
            Long vehicleId = Long.valueOf(data.get("vehicleId").toString());
            Long shopId = Long.valueOf(data.get("shopId").toString());
            String issueDescription = data.get("issueDescription").toString();
            Double lat = Double.valueOf(data.get("latitude").toString());
            Double lng = Double.valueOf(data.get("longitude").toString());

            Booking booking = bookingService.createBooking(ownerId, vehicleId, shopId, issueDescription, lat, lng);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint for Service Provider to change booking to ACCEPTED or REJECTED
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam Booking.BookingStatus status) {
        try {
            Booking updatedBooking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint for Vehicle Owner to finalize work and assign custom star ratings
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id, @RequestBody Map<String, Object> evaluation) {
        try {
            Integer rating = Integer.valueOf(evaluation.get("rating").toString());
            String reviewText = evaluation.get("reviewText").toString();

            Booking completedBooking = bookingService.completeAndRateBooking(id, rating, reviewText);
            return ResponseEntity.ok(completedBooking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to track personal emergency feedback history for an owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Booking>> getOwnerHistory(@PathVariable Long ownerId) {
        return ResponseEntity.ok(bookingService.getOwnerBookingHistory(ownerId));
    }

    // Endpoint to retrieve incoming queues for a workshop dashboard feed
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Booking>> getShopQueue(@PathVariable Long shopId) {
        return ResponseEntity.ok(bookingService.getShopBookings(shopId));
    }
}