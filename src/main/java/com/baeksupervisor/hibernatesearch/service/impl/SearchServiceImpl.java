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

        /*
        fuzzy queries : 키워드 유사 검색
                        Levenshtein distance 알고리즘을 기반으로 한 검색
                        withPrefixLength 로 무시되는 접두어의 길이를 세팅 (default=0 이지만, 0이 아닌 값을 사용하는 것이 좋음)
                        withThreshold(Deprecated) -> withEditDistanceUpTo 로 검색되는 문자열이 얼마나 다른지에 대해 세팅 (default=2)
         */
        Query luceneQuery = qb
                .keyword()
                .fuzzy()
//                .withThreshold(.8f)
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

        /*
        wildcard queries : 와일드카드 검색
                           '*' or '?' 를 이용해서 와일드카드 검색을 수행.
                           성능상에 이슈가 있을 수 있기 때문에 와일드카드 문자는 검색어 앞에서는 사용하지 않아야 한다.
         */
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

        /*
        phrase queries : 문장 검색
                         widSlop 인수를 추가하여 문장에서 허용되는 다른 단어의 수를 세팅할 수 있다.
         */
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
