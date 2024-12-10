package com.kitHub.Facilities_info.domain.facility;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kitHub.Facilities_info.domain.image.DetailedLocationImage;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "detailed_locations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailedLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private double rating;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    @JsonBackReference
    private Facility facility;

    @OneToMany(mappedBy = "detailedLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<DetailedLocationImage> images;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Builder
    public DetailedLocation(String location, double rating, Facility facility, double latitude, double longitude, Set<DetailedLocationImage> images) {
        this.location = location;
        this.rating = rating;
        this.facility = facility;
        this.latitude = latitude;
        this.longitude = longitude;
        this.images = (images == null) ? new HashSet<>() : images;
    }
}
