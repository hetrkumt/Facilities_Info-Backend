package com.kitHub.Facilities_info.repository.facility;

import com.kitHub.Facilities_info.domain.facility.FacilityGeoCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface FacilityGeoCoordinatesRepository extends JpaRepository<FacilityGeoCoordinates, Long> {

    @Query("SELECT g FROM FacilityGeoCoordinates g JOIN FETCH g.facility WHERE " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(g.latitude)) * cos(radians(g.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(g.latitude)))) < :radius")
    List<FacilityGeoCoordinates> findGeoCoordinatesWithinRadius(@Param("latitude") double latitude,
                                                                @Param("longitude") double longitude,
                                                                @Param("radius") double radius);

    // 위도와 경도를 통해 FacilityGeoCoordinates를 찾는 메서드
    @Query("SELECT g FROM FacilityGeoCoordinates g JOIN FETCH g.facility WHERE g.latitude = :latitude AND g.longitude = :longitude")
    FacilityGeoCoordinates findByLatitudeAndLongitude(@Param("latitude") double latitude, @Param("longitude") double longitude);
}
