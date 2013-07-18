package me.tfeng.tokenizers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public abstract class Tokenizer {
  private static final NumberFormat DECIMAL_FORMAT = NumberFormat.getInstance();
  static {
    DECIMAL_FORMAT.setMinimumFractionDigits(2);
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
    DECIMAL_FORMAT.setGroupingUsed(false);
  }
  
  private File input;
  private File output;
  
  protected File getInput() {
    return input;
  }
  
  protected File getOutput() {
    return output;
  }
  
  protected TokenizingResult process() throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(input));
    PrintWriter writer = new PrintWriter(new FileWriter(output));
    Date startTime = new Date();
    int lineCount = 0;
    int tokenCount = 0;
    try {
      String line = reader.readLine();
      while (line != null) {
        lineCount++;
        List<String> tokens = tokenize(line);
        StringBuffer buffer = new StringBuffer();
        for (String token : tokens) {
          tokenCount++;
          if (buffer.length() > 0) {
            buffer.append(" ");
          }
          buffer.append(token);
        }
        writer.println(buffer);
        line = reader.readLine();
      }
    } finally {
      reader.close();
      writer.close();
    }
    Date endTime = new Date();
    return new TokenizingResult(startTime, endTime, lineCount, tokenCount);
  }
  
  protected abstract List<String> tokenize(String line) throws IOException;
  
  public static void main(String[] args) throws IOException, ClassNotFoundException,
  InstantiationException, IllegalAccessException {
    if (args.length != 3) {
      System.err.println("Usage: java " + Tokenizer.class.getSimpleName()
                         + " <type> <input> <output>");
      System.exit(1);
    }
    
    String className = Tokenizer.class.getPackage().getName() + "." + args[0] + "Tokenizer";
    Class<?> clazz = Class.forName(className);
    Tokenizer tokenizer = (Tokenizer) clazz.newInstance();
    tokenizer.input = new File(args[1]);
    tokenizer.output = new File(args[2]);
    
    TokenizingResult result = tokenizer.process();
    
    System.out.println("Time spent: " + DECIMAL_FORMAT.format(result.getTimeInSeconds()) + " seconds");
    System.out.println("Lines     : " + result.lineCount);
    System.out.println("Tokens    : " + result.tokenCount);
    System.out.println("Lines/Sec : " + DECIMAL_FORMAT.format(result.getLinesPerSeconds()));
    System.out.println("Tokens/Sec: " + DECIMAL_FORMAT.format(result.getTokensPerSecond()));
  }
  
  protected class TokenizingResult {
    protected Date startTime;
    protected Date endTime;
    protected int lineCount;
    protected int tokenCount;
    
    public TokenizingResult(Date startTime, Date endTime, int lineCount, int tokenCount) {
      this.startTime = startTime;
      this.endTime = endTime;
      this.lineCount = lineCount;
      this.tokenCount = tokenCount;
    }
    
    public double getTimeInSeconds() {
      return (double) (endTime.getTime() - startTime.getTime()) / 1000;
    }
    
    public double getTokensPerSecond() {
      return (double) tokenCount / getTimeInSeconds();
    }
    
    public double getLinesPerSeconds() {
      return (double) lineCount / getTimeInSeconds();
    }
  }
}
