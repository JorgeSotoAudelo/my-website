package com.jorgesoto.websiteapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jorgesoto.websiteapi.services.ResumeService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ResumeService resumeService;
    
    @GetMapping("/resume")
    public ResponseEntity<Resource> getResume() {
        return resumeService.getResume();
    }
}