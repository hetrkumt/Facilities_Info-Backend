package com.kitHub.Facilities_info.controller.facility;

import com.kitHub.Facilities_info.domain.facility.DetailedLocation;

import com.kitHub.Facilities_info.dto.facility.CreateDetailedLocationRequest;
import com.kitHub.Facilities_info.service.facility.DetailedLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/detailed-locations")
@RequiredArgsConstructor
public class DetailedLocationController {
    private final DetailedLocationService detailedLocationService;

    @GetMapping("/{detailedLocationId}")
    public ResponseEntity<?> getDetailedLocationById(@PathVariable Long detailedLocationId) {
        try {
            DetailedLocation detailedLocation = detailedLocationService.findById(detailedLocationId);
            return ResponseEntity.ok(detailedLocation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("Detailed location not found");
        }
    }

    @PostMapping
    public ResponseEntity<String> createDetailedLocation(@ModelAttribute CreateDetailedLocationRequest request) {
        try {
            detailedLocationService.saveDetailedLocation(request);
            return ResponseEntity.ok("Detailed location created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to create detailed location");
        }
    }

    @DeleteMapping("/{detailedLocationId}")
    public ResponseEntity<String> deleteDetailedLocation(@PathVariable Long detailedLocationId) {
        try {
            detailedLocationService.deleteDetailedLocation(detailedLocationId);
            return ResponseEntity.ok("Detailed location deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete detailed location");
        }
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<ByteArrayResource> getDetailedLocationImage(@PathVariable String fileName) {
        try {
            byte[] imageData = detailedLocationService.getDetailedLocationImage(fileName);
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
