package com.baeksupervisor.hibernatesearch.persistence.entity;

import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;

@Indexed
@Entity
@Table(name = "brand")

//@AnalyzerDefs({
//
//        @AnalyzerDef(name = "autocompleteEdgeAnalyzer",
//
//// Split input into tokens according to tokenizer
//                tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class),
//
//                filters = {
//                        // Normalize token text to lowercase, as the user is unlikely to
//                        // care about casing when searching for matches
//                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
//                                @Parameter(name = "pattern",value = "([^a-zA-Z0-9\\.])"),
//                                @Parameter(name = "replacement", value = " "),
//                                @Parameter(name = "replace", value = "all") }),
//                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                        @TokenFilterDef(factory = StopFilterFactory.class),
//                        // Index partial words starting at the front, so we can provide
//                        // Autocomplete functionality
//                        @TokenFilterDef(factory = EdgeNGramFilterFactory.class, params = {
//                                @Parameter(name = "minGramSize", value = "1"),
//                                @Parameter(name = "maxGramSize", value = "100") }) }),
//
//        @AnalyzerDef(name = "autocompleteNGramAnalyzer",
//
//// Split input into tokens according to tokenizer
//                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//
//                filters = {
//                        // Normalize token text to lowercase, as the user is unlikely to
//                        // care about casing when searching for matches
//                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
//                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                        @TokenFilterDef(factory = NGramFilterFactory.class, params = {
//                                @Parameter(name = "minGramSize", value = "1"),
//                                @Parameter(name = "maxGramSize", value = "100") }),
//                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
//                                @Parameter(name = "pattern",value = "([^a-zA-Z0-9\\.])"),
//                                @Parameter(name = "replacement", value = " "),
//                                @Parameter(name = "replace", value = "all") })
//                }),
//
//        @AnalyzerDef(name = "standardAnalyzer",
//
//// Split input into tokens according to tokenizer
//                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
//
//                filters = {
//                        // Normalize token text to lowercase, as the user is unlikely to
//                        // care about casing when searching for matches
//                        @TokenFilterDef(factory = WordDelimiterFilterFactory.class),
//                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
//                        @TokenFilterDef(factory = PatternReplaceFilterFactory.class, params = {
//                                @Parameter(name = "pattern", value = "([^a-zA-Z0-9\\.])"),
//                                @Parameter(name = "replacement", value = " "),
//                                @Parameter(name = "replace", value = "all") })
//                }) // Def
//})

@AnalyzerDefs({
        @AnalyzerDef(name = "customanalyzer1",
                tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
                filters = {
                        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = {
                                @Parameter(name = "language", value = "English")
                        })
                }),
        @AnalyzerDef(name = "customanalyzer2",
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
public class Brand {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    @Fields({
            @Field(name = "name1", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer1")),
            @Field(name = "name2", index = Index.YES, analyze = Analyze.YES, store = Store.NO, analyzer = @Analyzer(definition = "customanalyzer2"))
    })
//    @Fields({
//            @Field(name = "name", index = Index.YES, store = Store.YES, analyze = Analyze.YES, analyzer = @Analyzer(definition = "standardAnalyzer")),
//            @Field(name = "edgeNGramName", index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteEdgeAnalyzer")),
//            @Field(name = "nGramName", index = Index.YES, store = Store.NO, analyze = Analyze.YES, analyzer = @Analyzer(definition = "autocompleteNGramAnalyzer"))
//    })
//    @Field(name = "name", index = Index.YES, store = Store.YES, analyze = Analyze.YES)
//    @SortableFields({
//            @SortableField(forField = "name1"),
//            @SortableField(forField = "name2"),
//    })
    private String name;

    public Brand() {}

    public Brand(String name) {
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
