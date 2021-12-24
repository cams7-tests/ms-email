/** */
package br.cams7.tests.ms.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** @author CIANDT\cmagalhaes */
public final class EmailEntityTestData {

  public static UUID EMAIL_ID = UUID.fromString("c7095a67-4df4-4642-af27-c25ac518affe");
  public static String OWNER_REF = "56317992002";
  public static String EMAIL_FROM = "from@tests.cams7.br";
  public static String EMAIL_TO = "to@tests.cams7.br";
  public static String MESSAGE_SUBJECT = "Message subject";
  public static String MESSAGE_TEXT = "Message text";
  public static LocalDateTime EMAIL_SENT_DATE =
      LocalDateTime.parse(
          "2021-12-21 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  public static EmailStatusEnum EMAIL_STATUS = EmailStatusEnum.SENT;

  public static EmailEntity defaultEmailEntity() {
    return EmailEntity.builder()
        .emailId(EMAIL_ID)
        .ownerRef(OWNER_REF)
        .emailFrom(EMAIL_FROM)
        .emailTo(EMAIL_TO)
        .subject(MESSAGE_SUBJECT)
        .text(MESSAGE_TEXT)
        .emailSentDate(EMAIL_SENT_DATE)
        .emailStatus(EMAIL_STATUS)
        .build();
  }
}
