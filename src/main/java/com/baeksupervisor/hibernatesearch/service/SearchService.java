package com.baeksupervisor.hibernatesearch.service;

import com.baeksupervisor.hibernatesearch.persistence.entity.News;
import com.baeksupervisor.hibernatesearch.persistence.entity.Product;

import java.util.List;

public interface SearchService {

    List<News> fuzzySearch(String searchTerm);

    List<Product> simpleSearchProduct(String searchTerm);
    List<Product> keywordSearchProduct(String searchTerm);
    List<Product> fuzzySearchProduct(String searchTerm);
    List<Product> wildcardSearchProduct(String searchTerm);
    List<Product> phraseSearchProduct(String searchTerm);
}
