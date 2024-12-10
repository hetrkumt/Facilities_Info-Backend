package com.kitHub.Facilities_info.controller;

import com.kitHub.Facilities_info.domain.user.Report.Report;
import com.kitHub.Facilities_info.dto.CreateReportRequest;
import com.kitHub.Facilities_info.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/{contentType}/{contentId}")
    public ResponseEntity<Report> createReport(@PathVariable String contentType,
                                               @PathVariable Long contentId,
                                               @RequestBody CreateReportRequest request) {
        try {
            Report report = reportService.createReport(contentType, contentId, request);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 잘못된 입력 시 400 응답
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 오류 반환
        }
    }

}
