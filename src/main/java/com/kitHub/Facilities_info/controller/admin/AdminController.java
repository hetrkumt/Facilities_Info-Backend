//package com.kitHub.Facilities_info.controller.admin;
//
//import com.kitHub.Facilities_info.domain.user.Report.Block;
//import com.kitHub.Facilities_info.domain.user.Report.Report;
//import com.kitHub.Facilities_info.domain.facility.Facility;
//import com.kitHub.Facilities_info.dto.UpdateFacilityInfo;
//import com.kitHub.Facilities_info.service.AdminService;
//import com.kitHub.Facilities_info.service.facility.FacilityService;
//import com.kitHub.Facilities_info.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("api/admin")
//public class AdminController {
//
//    @Autowired
//    private AdminService adminService;
//    @Autowired
//    private ReportService reportService;
//    @Autowired
//    private FacilityService facilityService;
//
//    @DeleteMapping("/delete/review/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
//        try {
//            adminService.deleteReview(reviewId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @DeleteMapping("/delete/post/{postId}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
//        try {
//            adminService.deletePost(postId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @DeleteMapping("/delete/comment/{commentId}")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
//        try {
//            adminService.deleteComment(commentId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @PostMapping("/block/{userId}")
//    public ResponseEntity<String> blockUser(@PathVariable Long userId) {
//        try {
//            Block block = adminService.blockUser(userId);
//            return ResponseEntity.ok("User blocked successfully.");
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(409).body(e.getMessage());  // 이미 블록된 상태 알림
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("An error occurred.");
//        }
//    }
//
//    @PostMapping("/unblock/{userId}")
//    public ResponseEntity<Void> unblockUser(@PathVariable Long userId) {
//        try {
//            adminService.unblockUser(userId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @GetMapping("/user-block/{userId}")
//    public ResponseEntity<Block> getUserBlockRecord(@PathVariable Long userId) {
//        try {
//            Block block = adminService.getUserBlockRecord(userId);
//            return ResponseEntity.ok(block);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @GetMapping("/blocks")
//    public ResponseEntity<List<Block>> getAllBlockRecords() {
//        try {
//            List<Block> blocks = adminService.getAllBlockRecords();
//            return ResponseEntity.ok(blocks);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Report>> getReportsByUser(@PathVariable Long userId) {
//        try {
//            List<Report> reports = reportService.getReportsByUser(userId);
//            return ResponseEntity.ok(reports);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(null); // 잘못된 입력 시 400 응답
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 오류 반환
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Report>> getAllReports() {
//        try {
//            List<Report> reports = reportService.getAllReports();
//            return ResponseEntity.ok(reports);
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 오류 반환
//        }
//    }
//
//    @PutMapping("update/facility/info/{facilityId}")
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
//
//}
