package com.kitHub.Facilities_info.repository.facility.detailedLocation;

import com.kitHub.Facilities_info.domain.image.DetailedLocationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailedLocationImageRepository extends JpaRepository<DetailedLocationImage, Long> {
    // 필요한 추가 메서드를 정의할 수 있습니다.
}
