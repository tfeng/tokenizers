package me.tfeng.tokenizers;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class EnglishTokenizer extends Tokenizer {
  private Analyzer analyzer = new Analyzer() {
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
      StandardTokenizer t = new StandardTokenizer(Version.LUCENE_43, reader);
      return new TokenStreamComponents(t, new LowerCaseFilter(Version.LUCENE_43, t));
    }
  };
  
  protected List<String> tokenize(String line) throws IOException {
    TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(line));
    tokenStream.reset();
    CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
    List<String> tokens = new ArrayList<String>();
    while (tokenStream.incrementToken()) {
      tokens.add(termAttribute.toString());
    }
    return tokens;
  }
}
