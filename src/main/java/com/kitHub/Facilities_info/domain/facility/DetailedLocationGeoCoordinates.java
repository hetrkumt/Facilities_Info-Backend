//package com.kitHub.Facilities_info.domain.facility;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "detailed_location_geo_coordinates")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Setter
//public class DetailedLocationGeoCoordinates {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private double latitude;
//
//    @Column(nullable = false)
//    private double longitude;
//
//    @OneToOne(mappedBy = "detailedLocationGeoCoordinates")
//    @JsonBackReference
//    private DetailedLocation detailedLocation;
//
//    @Builder
//    public DetailedLocationGeoCoordinates(double latitude, double longitude) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//
//    public void setDetailedLocation(DetailedLocation detailedLocation) {
//        this.detailedLocation = detailedLocation;
//    }
//}
//
