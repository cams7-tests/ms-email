package br.cams7.tests.ms.infra.persistence.model;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_email")
public class EmailModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id_email", nullable = false)
  private UUID emailId;

  @Column(name = "owner_ref", nullable = false)
  private String ownerRef;

  @Column(name = "email_from", nullable = false)
  private String emailFrom;

  @Column(name = "email_to", nullable = false)
  private String emailTo;

  @Column(name = "subject", nullable = false)
  private String subject;

  @Column(name = "text", columnDefinition = "TEXT", nullable = false)
  private String text;

  @Column(name = "email_sent_date", nullable = false)
  private LocalDateTime emailSentDate;

  @Column(name = "email_status", nullable = false)
  private EmailStatusEnum emailStatus;
}
