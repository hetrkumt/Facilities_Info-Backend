package com.kitHub.Facilities_info.domain.image;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kitHub.Facilities_info.domain.facility.DetailedLocation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detailed_location_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class DetailedLocationImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "detailed_location_id")
    @JsonBackReference
    private DetailedLocation detailedLocation;

    @Builder
    public DetailedLocationImage(String url, DetailedLocation detailedLocation) {
        this.url = url;
        this.detailedLocation = detailedLocation;
    }
}
