package com.toystorage.backend.services.packages;

import com.toystorage.backend.models.packages.PackageBox;
import com.toystorage.backend.repository.packages.PackageBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Service kiện hàng
 */

@Service

@RequiredArgsConstructor

public class PackageService {

    private final PackageBoxRepository packageBoxRepository;

    /*
     * Tạo kiện hàng
     */
    public PackageBox createPackage() {

        PackageBox box =
                PackageBox.builder()

                        .packageCode(
                                "PKG-" + System.currentTimeMillis()
                        )

                        .build();

        return packageBoxRepository.save(box);
    }

    /*
     * Danh sách kiện
     */
    public List<PackageBox> getAllPackages() {

        return packageBoxRepository.findAll();
    }
}