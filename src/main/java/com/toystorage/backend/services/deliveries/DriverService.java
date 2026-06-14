package com.toystorage.backend.services.deliveries;

import com.toystorage.backend.exceptions.BadRequest;
import com.toystorage.backend.models.deliveries.Driver;
import com.toystorage.backend.repository.deliveries.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new BadRequest("DRIVER_NOT_FOUND"));
    }

    public Driver createDriver(Driver driver) {
        if (driver == null) {
            throw new BadRequest("DRIVER_REQUIRED");
        }

        if (driver.getDriverCode() == null || driver.getDriverCode().isBlank()) {
            throw new BadRequest("DRIVER_CODE_REQUIRED");
        }

        if (driverRepository.existsByDriverCode(driver.getDriverCode())) {
            throw new BadRequest("DRIVER_CODE_EXISTS");
        }

        return driverRepository.save(driver);
    }

    public Driver updateDriver(Long id, Driver driver) {
        Driver oldDriver = getDriverById(id);

        if (driver.getName() != null) {
            oldDriver.setName(driver.getName());
        }

        if (driver.getPhone() != null) {
            oldDriver.setPhone(driver.getPhone());
        }

        if (driver.getVehicleNumber() != null) {
            oldDriver.setVehicleNumber(driver.getVehicleNumber());
        }

        if (driver.getActive() != null) {
            oldDriver.setActive(driver.getActive());
        }

        return driverRepository.save(oldDriver);
    }

    public void deleteDriver(Long id) {
        Driver driver = getDriverById(id);
        driverRepository.delete(driver);
    }
}