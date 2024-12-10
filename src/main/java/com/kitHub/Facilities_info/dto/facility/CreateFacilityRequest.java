package com.kitHub.Facilities_info.dto.facility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateFacilityRequest {
    private double latitude;
    private double longitude;
    private String name;
    private String address;
    private String description;
    private String type;
    private List<MultipartFile> images;
}
