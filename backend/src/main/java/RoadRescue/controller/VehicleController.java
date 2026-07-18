package RoadRescue.controller;

import RoadRescue.model.Vehicle;
import RoadRescue.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Endpoint to register a new vehicle under an owner
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(@RequestParam Long ownerId, @RequestBody Vehicle vehicle) {
        try {
            Vehicle newVehicle = vehicleService.addVehicle(ownerId, vehicle);
            return ResponseEntity.ok(newVehicle);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to get all vehicles belonging to a specific owner
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByOwner(ownerId));
    }
}