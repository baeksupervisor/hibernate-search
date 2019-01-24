package com.baeksupervisor.hibernatesearch.persistence.repository;

import com.baeksupervisor.hibernatesearch.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
