package RoadRescue.service;

import RoadRescue.model.ServiceShop;
import RoadRescue.model.User;
import RoadRescue.repository.ServiceShopRepository;
import RoadRescue.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceShopService {

    @Autowired
    private ServiceShopRepository serviceShopRepository;

    @Autowired
    private UserRepository userRepository;

    public ServiceShop addShop(Long providerId, ServiceShop shop) {
        User provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Service Provider not found!"));

        if (provider.getRole() != User.Role.SERVICE_PROVIDER) {
            throw new RuntimeException("Only registered Service Providers can add business places!");
        }

        shop.setProvider(provider);
        return serviceShopRepository.save(shop);
    }

    public List<ServiceShop> getShopsByProvider(Long providerId) {
        return serviceShopRepository.findByProviderId(providerId);
    }

    // FIX: Update this method to call the new containing/ignore-case repository method
    public List<ServiceShop> searchShopsByServiceType(String serviceType) {
        return serviceShopRepository.findByServiceTypeContainingIgnoreCase(serviceType);
    }

    public List<User> getAllApprovedProviders() {
        return userRepository.findByRole(User.Role.SERVICE_PROVIDER);
    }
}