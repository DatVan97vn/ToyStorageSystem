package com.toystorage.backend.dto.response.manifests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManifestPackageResponse {

    private Long id;

    private Long packageId;

    private String boxCode;

    private String status;
}