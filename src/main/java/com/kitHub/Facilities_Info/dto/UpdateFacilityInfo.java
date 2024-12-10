package com.kitHub.Facilities_info.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class UpdateFacilityInfo {
    private List<MultipartFile> images;
    private String name;
    private String address;
    private String description;
    private String detailedLocation;
}
