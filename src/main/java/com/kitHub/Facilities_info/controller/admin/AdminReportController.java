package com.kitHub.Facilities_info.controller.admin;

import com.kitHub.Facilities_info.domain.user.Report.Report;
import com.kitHub.Facilities_info.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/reports")
public class AdminReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getReportsByUser(@PathVariable Long userId) {
        try {
            List<Report> reports = reportService.getReportsByUser(userId);
            return ResponseEntity.ok(reports);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 입력 시 400 응답
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 오류 반환
        }
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        try {
            List<Report> reports = reportService.getAllReports();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 오류 반환
        }
    }
}
