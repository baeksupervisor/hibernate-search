package com.baeksupervisor.hibernatesearch.controller;

import com.baeksupervisor.hibernatesearch.persistence.entity.Product;
import com.baeksupervisor.hibernatesearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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


    @GetMapping(value = "/products")
    public ResponseEntity searchProducts(@RequestParam(value = "q", required = true) String q,
                                         @RequestParam(value = "type", required = true, defaultValue = "keyword") String type) {

        List<Product> list = new ArrayList<>();
        switch (type) {
            case "simple":
                list = searchService.simpleSearchProduct(q);
                break;
            case "keyword":
                list = searchService.keywordSearchProduct(q);
                break;
            case "fuzzy":
                list = searchService.fuzzySearchProduct(q);
                break;
            case "wildcard":
                list = searchService.wildcardSearchProduct(q);
                break;
            case "phrase":
                list = searchService.phraseSearchProduct(q);
                break;
        }

        return ResponseEntity.ok(list);
    }
}
