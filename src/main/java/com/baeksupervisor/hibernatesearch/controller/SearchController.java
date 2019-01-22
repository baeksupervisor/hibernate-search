package com.baeksupervisor.hibernatesearch.controller;

import com.baeksupervisor.hibernatesearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "")
    public ResponseEntity search(@RequestParam(value = "q", required = true) String q) {
        log.info("query={}", q);

//        searchService.
        return ResponseEntity.ok(null);
    }
}
