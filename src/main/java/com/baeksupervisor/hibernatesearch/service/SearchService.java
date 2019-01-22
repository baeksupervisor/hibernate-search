package com.baeksupervisor.hibernatesearch.service;

import com.baeksupervisor.hibernatesearch.persistence.entity.News;

import java.util.List;

public interface SearchService {

    List<News> fuzzySearch(String searchTerm);
}
