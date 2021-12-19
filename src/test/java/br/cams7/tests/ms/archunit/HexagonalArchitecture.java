/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyAnyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexagonalArchitecture {

  private final String basePackage;
  private Adapters adapters;
  private ApplicationLayer applicationLayer;
  private String configurationPackage;
  private List<String> domainPackages = new ArrayList<>();

  public static HexagonalArchitecture boundedContext(String basePackage) {
    return new HexagonalArchitecture(basePackage);
  }

  public HexagonalArchitecture(String basePackage) {
    this.basePackage = basePackage;
  }

  public Adapters withAdaptersLayer(String adaptersPackage) {
    this.adapters = new Adapters(getFullQualifiedPackage(basePackage, adaptersPackage), this);
    return this.adapters;
  }

  public HexagonalArchitecture withDomainLayer(String domainPackage) {
    this.domainPackages.add(getFullQualifiedPackage(basePackage, domainPackage));
    return this;
  }

  public ApplicationLayer withApplicationLayer(String applicationPackage) {
    this.applicationLayer =
        new ApplicationLayer(getFullQualifiedPackage(basePackage, applicationPackage), this);
    return this.applicationLayer;
  }

  public HexagonalArchitecture withConfiguration(String packageName) {
    this.configurationPackage = getFullQualifiedPackage(basePackage, packageName);
    return this;
  }

  private void domainDoesNotDependOnOtherPackages(JavaClasses classes) {
    denyAnyDependency(
        this.domainPackages, Collections.singletonList(adapters.getBasePackage()), classes);
    denyAnyDependency(
        this.domainPackages, Collections.singletonList(applicationLayer.getBasePackage()), classes);
  }

  public void check(JavaClasses classes) {
    this.adapters.doesNotContainEmptyPackages();
    this.adapters.dontDependOnEachOther(classes);
    this.adapters.doesNotDependOn(this.configurationPackage, classes);
    this.applicationLayer.doesNotContainEmptyPackages();
    this.applicationLayer.doesNotDependOn(this.adapters.getBasePackage(), classes);
    this.applicationLayer.doesNotDependOn(this.configurationPackage, classes);
    this.applicationLayer.incomingAndOutgoingPortsDoNotDependOnEachOther(classes);
    this.domainDoesNotDependOnOtherPackages(classes);
  }
}
