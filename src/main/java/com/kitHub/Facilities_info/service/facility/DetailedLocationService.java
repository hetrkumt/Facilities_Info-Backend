package com.kitHub.Facilities_info.service.facility;

import com.kitHub.Facilities_info.domain.facility.DetailedLocation;
import com.kitHub.Facilities_info.domain.facility.Facility;
import com.kitHub.Facilities_info.domain.image.DetailedLocationImage;
import com.kitHub.Facilities_info.dto.facility.CreateDetailedLocationRequest;
import com.kitHub.Facilities_info.repository.facility.detailedLocation.DetailedLocationRepository;
import com.kitHub.Facilities_info.repository.facility.detailedLocation.DetailedLocationImageRepository;
import com.kitHub.Facilities_info.repository.facility.FacilityRepository;
import com.kitHub.Facilities_info.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetailedLocationService {
    private final DetailedLocationRepository detailedLocationRepository;
    private final FacilityRepository facilityRepository;
    private final DetailedLocationImageRepository detailedLocationImageRepository;
    private final ImageService imageService; // ImageService 주입받기

    // 상세 위치를 ID로 조회
    public DetailedLocation findById(Long detailedLocationId) {
        Optional<DetailedLocation> detailedLocation = detailedLocationRepository.findById(detailedLocationId);
        if (detailedLocation.isPresent()) {
            return detailedLocation.get();
        } else {
            throw new IllegalArgumentException("Detailed Location not found");
        }
    }

    // 상세 위치 생성
    public DetailedLocation saveDetailedLocation(CreateDetailedLocationRequest request) throws IOException {
        Optional<Facility> facilityOptional = facilityRepository.findById(request.getFacilityId());
        if (!facilityOptional.isPresent()) {
            throw new IllegalArgumentException("Facility not found");
        }

        Facility facility = facilityOptional.get();

        DetailedLocation detailedLocation = DetailedLocation.builder()
                .location(request.getLocation())
                .rating(request.getRating())
                .facility(facility)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        detailedLocationRepository.save(detailedLocation);

        // 이미지 리스트가 null일 경우 빈 리스트로 초기화
        List<MultipartFile> images = (request.getImages() != null) ? request.getImages() : new ArrayList<>();
        saveDetailedLocationImages(detailedLocation, images);

        return detailedLocation;
    }

    // 상세 위치 이미지 저장
    private void saveDetailedLocationImages(DetailedLocation detailedLocation, List<MultipartFile> images) throws IOException {
        // ImageService를 사용하여 이미지 파일 저장
        List<String> imageFileNames = imageService.saveFacilityImageFiles(images, "detailed-locations");

        // 저장된 이미지 파일 이름으로 DetailedLocationImage 엔티티 생성 및 저장
        for (String imageFileName : imageFileNames) {
            DetailedLocationImage detailedLocationImage = DetailedLocationImage.builder()
                    .url(imageFileName)
                    .detailedLocation(detailedLocation)
                    .build();
            detailedLocationImageRepository.save(detailedLocationImage);
            detailedLocation.getImages().add(detailedLocationImage);
        }
    }

    // 상세 위치 삭제
    public void deleteDetailedLocation(Long detailedLocationId) {
        detailedLocationRepository.deleteById(detailedLocationId);
    }

    public byte[] getDetailedLocationImage(String fileName) throws IOException {
        return imageService.getImage("detailed-locations", fileName);
    }
}
