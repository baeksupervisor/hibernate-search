package com.baeksupervisor.hibernatesearch.persistence.entity;

import lombok.Data;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Indexed
@Data
@Entity(name = "news")
@AnalyzerDefs({
        @AnalyzerDef(name = "newsAnalyzer1",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                                @Parameter(name = "language", value = "English")
                        })
                }),
        @AnalyzerDef(name = "newsAnalyzer2",
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
public class News {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Column(name = "news_no")
    private Integer newsroomNo;

    @Column(name = "link")
    private String link;

    @Column(name = "title")
    @Fields({
            @Field(name = "title1", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "newsAnalyzer1")),
            @Field(name = "title2", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "newsAnalyzer2"))
    })
    private String title;

    @Column(name = "description", length = 1024)
    @Fields({
            @Field(name = "description1", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "newsAnalyzer1")),
            @Field(name = "description2", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "newsAnalyzer2"))
    })
    private String description;

    @Column(name = "image")
    private String image;
}