package RoadRescue.service;

import RoadRescue.model.User;
import RoadRescue.model.Vehicle;
import RoadRescue.repository.UserRepository;
import RoadRescue.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    // Logic for a vehicle owner to add a vehicle
    public Vehicle addVehicle(Long ownerId, Vehicle vehicle) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Vehicle Owner not found!"));
        
        if (owner.getRole() != User.Role.VEHICLE_OWNER) {
            throw new RuntimeException("Only registered Vehicle Owners can add vehicles!");
        }

        // Link the vehicle to its owner (OOP Association)
        vehicle.setOwner(owner);
        return vehicleRepository.save(vehicle);
    }

    // Logic to fetch all vehicles belonging to a specific owner
    public List<Vehicle> getVehiclesByOwner(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }
}