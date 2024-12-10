package com.kitHub.Facilities_info.service.facility;

import com.kitHub.Facilities_info.domain.facility.FacilityGeoCoordinates;
import com.kitHub.Facilities_info.repository.facility.FacilityGeoCoordinatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoCoordinatesService {

    private final FacilityGeoCoordinatesRepository facilityGeoCoordinatesRepository;

    public List<FacilityGeoCoordinates> getGeoCoordinatesWithinRadius(double latitude, double longitude, double radius) {
        return facilityGeoCoordinatesRepository.findGeoCoordinatesWithinRadius(latitude, longitude, radius);
    }
}
