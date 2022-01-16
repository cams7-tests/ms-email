package br.cams7.tests.ms.infra.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.Resource;

public final class FileUtils {

  private static final String EMPTY = "";
  private static final String NEW_LINE = "\n";
  private static final String CARRIAGE_RETURN = "\r";
  private static final String HORIZONTAL_TAB = "\t";

  public static String getContentFile(Resource dataResource) throws IOException {
    String content = Files.readString(loadContentFile(dataResource), StandardCharsets.UTF_8);
    return content
        .replaceAll(NEW_LINE, EMPTY)
        .replaceAll(CARRIAGE_RETURN, EMPTY)
        .replaceAll(HORIZONTAL_TAB, EMPTY);
  }

  private static Path loadContentFile(Resource dataResource) throws IOException {
    return dataResource.getFile().toPath();
  }
}
