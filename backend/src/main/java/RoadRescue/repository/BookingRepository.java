package RoadRescue.repository;

import RoadRescue.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Fetch booking history for a Vehicle Owner
    List<Booking> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    
    // Fetch incoming or past bookings for a specific Service Shop
    List<Booking> findByShopIdOrderByCreatedAtDesc(Long shopId);
}