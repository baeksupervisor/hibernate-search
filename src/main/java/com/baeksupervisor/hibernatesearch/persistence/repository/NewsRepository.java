package com.baeksupervisor.hibernatesearch.persistence.repository;

import com.baeksupervisor.hibernatesearch.persistence.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Integer> {

}
