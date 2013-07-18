package me.tfeng.tokenizers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class SimplifiedChineseTokenizer extends Tokenizer {
  private Seg seg = new ComplexSeg(Dictionary.getInstance());
  
  static {
    LogManager.getLogManager().reset();
  }

  protected List<String> tokenize(String line) throws IOException {
    MMSeg mmSeg = new MMSeg(new StringReader(line), seg);
    List<String> tokens = new ArrayList<String>();
    Word word = mmSeg.next();
    while (word != null) {
      tokens.add(word.getString());
      word = mmSeg.next();
    }
    return tokens;
  }
}
