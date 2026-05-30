package com.jorgesoto.websiteapi.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface ResumeService {
    ResponseEntity<Resource> getResume();
}
