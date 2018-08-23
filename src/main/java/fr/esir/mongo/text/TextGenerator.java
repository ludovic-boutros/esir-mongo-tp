package fr.esir.mongo.text;

import org.springframework.stereotype.Component;

/**
 *
 * @author lboutros
 */
@Component
public class TextGenerator {

  private final static String SENTENCE_MODEL = "blah. ";

  public String generateText(int sentenceCount) {
    //TODO implement a real sentence generator
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < sentenceCount; i++) {
      builder.append(SENTENCE_MODEL);
    }
    return builder.toString();
  }
}
