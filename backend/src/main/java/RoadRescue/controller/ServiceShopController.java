package RoadRescue.controller;

import RoadRescue.model.ServiceShop;
import RoadRescue.model.User;
import RoadRescue.service.ServiceShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@CrossOrigin(origins = "*")
public class ServiceShopController {

    @Autowired
    private ServiceShopService serviceShopService;

    // Endpoint for service provider to add a workshop location
    @PostMapping("/add")
    public ResponseEntity<?> addShop(@RequestParam Long providerId, @RequestBody ServiceShop shop) {
        try {
            ServiceShop newShop = serviceShopService.addShop(providerId, shop);
            return ResponseEntity.ok(newShop);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to get all shops owned by a single provider
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<ServiceShop>> getShopsByProvider(@PathVariable Long providerId) {
        return ResponseEntity.ok(serviceShopService.getShopsByProvider(providerId));
    }

    // Endpoint for vehicle owners to search providers by specific service types
    @GetMapping("/search")
    public ResponseEntity<List<ServiceShop>> searchShops(@RequestParam String serviceType) {
        return ResponseEntity.ok(serviceShopService.searchShopsByServiceType(serviceType));
    }

    // Admin Endpoint: View all registered/approved service providers
    @GetMapping("/providers")
    public ResponseEntity<List<User>> getAllProviders() {
        return ResponseEntity.ok(serviceShopService.getAllApprovedProviders());
    }
}