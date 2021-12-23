package br.cams7.tests.ms;

import static br.cams7.tests.ms.archunit.HexagonalArchitecture.boundedContext;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

class DependencyRuleTests {

  @Test
  void validateRegistrationContextArchitecture() {
    boundedContext(EmailApplication.class.getPackageName())
        .withDomainLayer("domain")
        .withAdapterLayer("infra")
        .incoming("controller")
        .incoming("mq")
        .outgoing("persistence.repository")
        // .outgoing("persistence.model")
        .outgoing("smtp")
        .outgoing("mq.service")
        .outgoing("client")
        // .outgoing("client.response")
        .and()
        .withApplicationLayer("core")
        .services("service")
        .incomingPorts("port.in")
        .incomingPorts("port.in.exception")
        .outgoingPorts("port.out")
        .outgoingPorts("port.out.exception")
        .and()
        .withConfiguration("infra.configuration")
        .check(new ClassFileImporter().importPackages("br.cams7.tests.."));
  }
}
