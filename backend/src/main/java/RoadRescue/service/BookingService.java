package RoadRescue.service;

import RoadRescue.model.Booking;
import RoadRescue.model.ServiceShop;
import RoadRescue.model.User;
import RoadRescue.model.Vehicle;
import RoadRescue.repository.BookingRepository;
import RoadRescue.repository.ServiceShopRepository;
import RoadRescue.repository.UserRepository;
import RoadRescue.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceShopRepository serviceShopRepository;

    // 1. Logic for Vehicle Owner to request roadside help
    public Booking createBooking(Long ownerId, Long vehicleId, Long shopId, 
                                 String issueDescription, Double lat, Double lng) {
        
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found!"));
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found!"));
        ServiceShop shop = serviceShopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Service shop not found!"));

        Booking booking = new Booking();
        booking.setOwner(owner);
        booking.setVehicle(vehicle);
        booking.setShop(shop);
        booking.setIssueDescription(issueDescription);
        booking.setCustomerLatitude(lat);
        booking.setCustomerLongitude(lng);
        booking.setStatus(Booking.BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    // 2. Logic for Service Provider to Accept or Reject a request
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking record not found!"));
        
        // Safety check: Cannot directly force completion without customer authorization
        if (newStatus == Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Only vehicle owners can mark a booking as fully completed!");
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    // 3. Logic for Vehicle Owner to mark service as COMPLETED and drop a rating
    public Booking completeAndRateBooking(Long bookingId, Integer rating, String reviewText) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking record not found!"));

        if (booking.getStatus() != Booking.BookingStatus.ACCEPTED) {
            throw new RuntimeException("Can only complete bookings that have been accepted by a provider!");
        }

        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating scores must be between 1 and 5 stars.");
        }

        // 1. Save rating to the Booking
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setRating(rating);
        booking.setReviewText(reviewText);

        // 2. Calculate and update the Service Shop's overall rating
        ServiceShop shop = booking.getShop();
        
        // Handle nulls safely just in case it is a brand new shop
        int currentCount = shop.getReviewCount() != null ? shop.getReviewCount() : 0;
        double currentAverage = shop.getAverageRating() != null ? shop.getAverageRating() : 0.0;
        
        // The Math: ((Old Average * Old Count) + New Rating) / New Total Count
        double newAverage = ((currentAverage * currentCount) + rating) / (currentCount + 1);
        
        // Round it to one decimal place (e.g., 4.333333 becomes 4.3)
        newAverage = Math.round(newAverage * 10.0) / 10.0;
        
        // Update and save the Shop
        shop.setReviewCount(currentCount + 1);
        shop.setAverageRating(newAverage);
        serviceShopRepository.save(shop);

        // 3. Save and return the final Booking
        return bookingRepository.save(booking);
    }

    // 4. Fetch emergency history feed for a specific Vehicle Owner
    public List<Booking> getOwnerBookingHistory(Long ownerId) {
        return bookingRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId);
    }

    // 5. Fetch jobs checklist belonging to a specific Service Shop dashboard
    public List<Booking> getShopBookings(Long shopId) {
        return bookingRepository.findByShopIdOrderByCreatedAtDesc(shopId);
    }
}