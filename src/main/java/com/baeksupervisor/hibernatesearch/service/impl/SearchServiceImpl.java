package com.baeksupervisor.hibernatesearch.service.impl;

import com.baeksupervisor.hibernatesearch.persistence.entity.News;
import com.baeksupervisor.hibernatesearch.persistence.entity.Product;
import com.baeksupervisor.hibernatesearch.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @PostConstruct
    public void buildSearchIndex() throws InterruptedException {
        // 인덱스를 재생성
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    @Transactional
    @Override
    public List<Product> simpleSearchProduct(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query luceneQuery = qb
                .simpleQueryString()
                .onField("name")
                .matching(searchTerm)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);

        List<Product> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            log.error("{}", nre);
        }

        return list;
    }

    @Transactional
    @Override
    public List<Product> fuzzySearchProduct(String searchTerm) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query luceneQuery = qb
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(0)
                .onFields("name")
                .matching(searchTerm)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);

        List<Product> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            log.error("{}", nre);
        }

        return list;
    }

    @Transactional
    @Override
    public List<Product> keywordSearchProduct(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query luceneQuery = qb
                .keyword()
                .onFields("name")
                .matching(searchTerm)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);

        List<Product> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            log.error("{}", nre);
        }

        return list;
    }

    @Transactional
    @Override
    public List<Product> wildcardSearchProduct(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query luceneQuery = qb
                .keyword()
                .wildcard()
                .onFields("name")
                .matching(searchTerm.concat("*"))
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);

        List<Product> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            log.error("{}", nre);
        }

        return list;
    }

    @Transactional
    @Override
    public List<Product> phraseSearchProduct(String searchTerm) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        Query luceneQuery = qb
                .phrase()
                .withSlop(1)
                .onField("name")
                .sentence(searchTerm)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Product.class);

        List<Product> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            log.error("{}", nre);
        }

        return list;
    }

    @Transactional
    @Override
    public List<News> fuzzySearch(String searchTerm) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(News.class).get();
        Query luceneQuery = qb
                .keyword()
                .fuzzy()
                .withEditDistanceUpTo(1)
                .withPrefixLength(1)
                .onFields("title", "description")
                .matching(searchTerm)
                .createQuery();

        javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, News.class);

        // execute search

        List<News> list = null;
        try {
            list = jpaQuery.getResultList();
        } catch (NoResultException nre) {
            ;// do nothing

        }

        return list;


    }
}
