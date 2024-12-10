package com.kitHub.Facilities_info.domain.facility;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "facility_geo_coordinates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class FacilityGeoCoordinates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @OneToOne(mappedBy = "facilityGeoCoordinates")
    @JsonManagedReference
    private Facility facility;

    @Builder
    public FacilityGeoCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }
}
