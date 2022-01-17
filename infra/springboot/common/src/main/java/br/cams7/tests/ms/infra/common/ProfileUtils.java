package br.cams7.tests.ms.infra.common;

import static org.springframework.util.ObjectUtils.isEmpty;

import org.springframework.core.env.Environment;

public class ProfileUtils {

  public static final String REST_RABBITMQ_POSTGRES_PROFILE = "rest-rabbitmq-postgres";
  public static final String REST_RABBITMQ_MONOGODB_PROFILE = "rest-rabbitmq-mongodb";

  private static final String NO_PROFILE = "";
  private static final byte FOUND_PROFILE = 0;

  public static String getActiveProfile(Environment environment) {
    var activeProfiles = environment.getActiveProfiles();
    if (isEmpty(activeProfiles)) return NO_PROFILE;
    return activeProfiles[FOUND_PROFILE];
  }
}
