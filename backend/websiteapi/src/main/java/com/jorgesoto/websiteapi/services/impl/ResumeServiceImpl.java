package com.jorgesoto.websiteapi.services.impl;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jorgesoto.websiteapi.services.ResumeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    @Override
    public ResponseEntity<Resource> getResume() {
        Resource resume = new ClassPathResource("static/resume.pdf");
        if(!resume.exists()) return ResponseEntity.notFound().build();
        try {
            HttpHeaders headers = createHeaders(resume.contentLength());
            return ResponseEntity.ok()
            .headers(headers)
            .body(resume);
        } catch (IOException e) {
            log.error("Error loading resume: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private HttpHeaders createHeaders(Long contentLength) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resume.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, contentLength.toString());
        return headers;
    }
    
}
