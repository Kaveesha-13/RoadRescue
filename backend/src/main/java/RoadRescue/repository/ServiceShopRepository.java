package RoadRescue.repository;

import RoadRescue.model.ServiceShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceShopRepository extends JpaRepository<ServiceShop, Long> {
    
    // Fetch all shops managed by a specific Provider ID
    List<ServiceShop> findByProviderId(Long providerId);
    
    //  case-insensitive searching
    List<ServiceShop> findByServiceTypeContainingIgnoreCase(String serviceType);
}