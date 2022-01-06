package br.cams7.tests.ms.infra.persistence.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tb_email")
public class EmailModel {

  @Id
  @Column("id_email")
  private UUID emailId;

  @Column("owner_ref")
  private String ownerRef;

  @Column("email_from")
  private String emailFrom;

  @Column("email_to")
  private String emailTo;

  @Column("subject")
  private String subject;

  @Column("text")
  private String text;

  @Column("email_sent_date")
  private LocalDateTime emailSentDate;

  @Column("email_status")
  private byte emailStatus;
}
