package br.cams7.tests.ms.infra.dataprovider.model;

import static br.cams7.tests.ms.infra.dataprovider.model.EmailModel.COLLECTION;
import static java.util.Objects.isNull;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = COLLECTION)
public class EmailModel {

  public static final String COLLECTION = "email";
  public static final String FIELD_EMAIL_ID = "_id";
  public static final String FIELD_OWNER_REF = "owner_ref";
  public static final String FIELD_EMAIL_FROM = "email_from";
  public static final String FIELD_EMAIL_TO = "email_to";
  public static final String FIELD_SUBJECT = "subject";
  public static final String FIELD_TEXT = "text";
  public static final String FIELD_EMAIL_SENT_DATE = "email_sent_date";
  public static final String FIELD_EMAIL_STATUS = "email_status";

  @Id private String id;

  @Field(name = FIELD_OWNER_REF)
  private String ownerRef;

  @Field(name = FIELD_EMAIL_FROM)
  private String emailFrom;

  @Field(name = FIELD_EMAIL_TO)
  private String emailTo;

  @Field(name = FIELD_SUBJECT)
  private String subject;

  @Field(name = FIELD_TEXT)
  private String text;

  @Field(name = FIELD_EMAIL_SENT_DATE)
  private LocalDateTime emailSentDate;

  @Field(name = FIELD_EMAIL_STATUS)
  private EmailStatusEnum emailStatus;

  public void setEmailId(String emailId) {
    this.id = emailId;
  }

  public void setEmailId(UUID emailId) {
    if (!isNull(emailId)) setEmailId(String.valueOf(emailId));
  }
}
