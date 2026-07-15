package RoadRescue.repository;

import RoadRescue.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    // Fetch all vehicles belonging to a specific Owner ID
    List<Vehicle> findByOwnerId(Long ownerId);
}