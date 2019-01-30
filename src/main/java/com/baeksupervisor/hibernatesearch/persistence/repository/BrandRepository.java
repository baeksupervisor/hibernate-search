package com.baeksupervisor.hibernatesearch.persistence.repository;

import com.baeksupervisor.hibernatesearch.persistence.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
