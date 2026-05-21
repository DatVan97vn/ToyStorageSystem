package com.toystorage.backend.repository.packages;

import com.toystorage.backend.models.packages.PackageBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageBoxRepository
        extends JpaRepository<PackageBox, Long> {

    Optional<PackageBox> findByPackageCode(String packageCode);
}