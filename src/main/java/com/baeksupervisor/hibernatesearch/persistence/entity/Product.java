package com.baeksupervisor.hibernatesearch.persistence.entity;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Indexed
@Entity
@Table(name = "product")
@AnalyzerDefs({
        @AnalyzerDef(name = "productAnalyzer1",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                                @Parameter(name = "language", value = "English")
                        })
                }),
        @AnalyzerDef(name = "productAnalyzer2",
                tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(
                                factory = EdgeNGramFilterFactory.class, // Generate prefix tokens
                                params = {
                                        @Parameter(name = "minGramSize", value = "1"),
                                        @Parameter(name = "maxGramSize", value = "100")
                                }
                        )
                })

})
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Fields({
            @Field(name = "name1", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "productAnalyzer1")),
            @Field(name = "name2", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "productAnalyzer2"))
    })
    private String name;

    public Product() {}

    public Product(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
