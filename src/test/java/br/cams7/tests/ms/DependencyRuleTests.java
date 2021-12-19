package br.cams7.tests.ms;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import br.cams7.tests.ms.archunit.HexagonalArchitecture;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

public class DependencyRuleTests {

  @Test
  void validateRegistrationContextArchitecture() {
    HexagonalArchitecture.boundedContext("br.cams7.tests.ms")
        .withDomainLayer("domain")
        .withAdaptersLayer("infra")
        .incoming("controller")
        .outgoing("persistence")
        .and()
        .withApplicationLayer("core")
        .services("service")
        .incomingPorts("port.in")
        .outgoingPorts("port.out")
        .and()
        .withConfiguration("configuration")
        .check(new ClassFileImporter().importPackages("br.cams7.tests.."));
  }

  @Test
  void testPackageDependencies() {
    noClasses()
        .that()
        .resideInAPackage("br.cams7.tests.ms.domain..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("br.cams7.tests.ms.core..")
        .check(new ClassFileImporter().importPackages("br.cams7.tests.ms.."));
  }
}
