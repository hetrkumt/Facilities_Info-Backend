package com.kitHub.Facilities_info.dto.facility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateDetailedLocationRequest {
    private Long facilityId;
    private double latitude;
    private double longitude;
    private String location;
    private double rating;
    private List<MultipartFile> images;
}
