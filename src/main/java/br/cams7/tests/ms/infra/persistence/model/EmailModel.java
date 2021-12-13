package br.cams7.tests.ms.infra.persistence.model;

import br.cams7.tests.ms.core.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_email")
public class EmailModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_email")
  private UUID emailId;

  @Column(name = "owner_ref")
  private String ownerRef;

  @Column(name = "email_from")
  private String emailFrom;

  @Column(name = "email_to")
  private String emailTo;

  @Column(name = "subject")
  private String subject;

  @Column(name = "text", columnDefinition = "TEXT")
  private String text;

  @Column(name = "email_sent_date")
  private LocalDateTime emailSentDate;

  @Column(name = "email_status")
  private EmailStatusEnum emailStatus;
}
