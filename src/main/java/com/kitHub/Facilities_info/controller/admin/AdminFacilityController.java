//package com.kitHub.Facilities_info.controller.admin;
//
//import com.kitHub.Facilities_info.domain.facility.Facility;
//import com.kitHub.Facilities_info.dto.UpdateFacilityInfo;
//import com.kitHub.Facilities_info.service.facility.FacilityService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//
//@RestController
//@RequestMapping("api/admin/facilities")
//public class AdminFacilityController {
//
//    @Autowired
//    private FacilityService facilityService;
//
//    @PutMapping("/update/info/{facilityId}")
//    public ResponseEntity<?> updateFacility(@PathVariable Long facilityId, @ModelAttribute UpdateFacilityInfo updateFacilityInfo) {
//        try {
//            Facility savedFacility = facilityService.updateFacilityDetails(facilityId, updateFacilityInfo);
//            return ResponseEntity.ok(savedFacility);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();  // 예외 스택 트레이스 출력
//            return ResponseEntity.badRequest().body(e.getMessage()); // 잘못된 입력 시 400 응답
//        } catch (IOException e) {
//            e.printStackTrace();  // 예외 스택 트레이스 출력
//            return ResponseEntity.status(500).body("File processing error occurred");
//        } catch (Exception e) {
//            e.printStackTrace();  // 예외 스택 트레이스 출력
//            return ResponseEntity.status(500).body("An unexpected error occurred");
//        }
//    }
//}
