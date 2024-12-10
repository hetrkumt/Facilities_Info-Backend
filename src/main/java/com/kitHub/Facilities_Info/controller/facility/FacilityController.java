package com.kitHub.Facilities_info.controller.facility;

import com.kitHub.Facilities_info.domain.facility.Facility;

import com.kitHub.Facilities_info.domain.facility.FacilityType;
import com.kitHub.Facilities_info.dto.UpdateFacilityInfo;
import com.kitHub.Facilities_info.dto.facility.CreateFacilityRequest;
import com.kitHub.Facilities_info.service.facility.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FacilityController {
    @Autowired
    private FacilityService facilityService;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/facilities/search")
    public ResponseEntity<List<Facility>> searchFacilities(@RequestParam(required = false) String name,
                                                           @RequestParam(required = false) FacilityType type) {
        List<Facility> results;

        if (name != null && type != null) {
            results = facilityService.findByNameContainingAndType(name, type);
        } else if (name != null) {
            results = facilityService.findByNameContaining(name);
        } else if (type != null) {
            results = facilityService.findByType(type);
        } else {
            results = facilityService.findAllWithGeoCoordinates();
        }

        if (results.isEmpty()) {
            return ResponseEntity.status(404).body(results); // HTTP 404 Not Found
        } else {
            return ResponseEntity.ok(results); // HTTP 200 OK
        }
    }


    @GetMapping("facility/{facilityId}")
    public ResponseEntity<?> getFacilityById(@PathVariable Long facilityId) {
        try {
            Facility facility = facilityService.findById(facilityId);
            return ResponseEntity.ok(facility);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Facility not found");
        }
    }


    @PutMapping("/update/info/{facilityId}")
    public ResponseEntity<?> updateFacility(@PathVariable Long facilityId, @ModelAttribute UpdateFacilityInfo updateFacilityInfo) {
        try {
            Facility savedFacility = facilityService.updateFacilityDetails(facilityId, updateFacilityInfo);
            return ResponseEntity.ok(savedFacility);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.badRequest().body(e.getMessage()); // 잘못된 입력 시 400 응답
        } catch (IOException e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.status(500).body("File processing error occurred");
        } catch (Exception e) {
            e.printStackTrace();  // 예외 스택 트레이스 출력
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }

    @PostMapping("/facility")
    public ResponseEntity<String> createFacility(@ModelAttribute CreateFacilityRequest request) {
        try {
            facilityService.saveFacility(request);
            return ResponseEntity.ok("Facility created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to create facility");
        }
    }

    @DeleteMapping("/facility/{facilityId}")
    public ResponseEntity<String> deleteFacility(@PathVariable Long facilityId) {
        try {
            facilityService.deleteFacility(facilityId);
            return ResponseEntity.ok("Facility and its detailed locations deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete facility");
        }
    }
    @GetMapping("/facility/images/{fileName}")
    public ResponseEntity<ByteArrayResource> getFacilityImage(@PathVariable String fileName) {
        try {
            byte[] imageData = facilityService.getFacilityImage(fileName);
            ByteArrayResource resource = new ByteArrayResource(imageData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .contentLength(imageData.length)
                    .body(resource);
        } catch (IOException e) {
            // 예외가 발생하면 404 Not Found 상태와 함께 오류 메시지를 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}



