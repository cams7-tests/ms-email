package br.cams7.tests.ms.infra.outbound.persistence;

import br.cams7.tests.ms.infra.outbound.persistence.models.EmailModel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataSqlEmailRepository extends JpaRepository<EmailModel, UUID> {}
