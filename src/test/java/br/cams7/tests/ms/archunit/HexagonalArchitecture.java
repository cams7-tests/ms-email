/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyAnyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HexagonalArchitecture {

  private final String basePackage;
  private AdapterLayer adapterLayer;
  private ApplicationLayer applicationLayer;
  private String configurationPackage;
  private final Set<String> domainPackages = new HashSet<>();

  public static HexagonalArchitecture boundedContext(String basePackage) {
    return new HexagonalArchitecture(basePackage);
  }

  private HexagonalArchitecture(String basePackage) {
    this.basePackage = basePackage;
  }

  public AdapterLayer withAdapterLayer(String adaptersPackage) {
    this.adapterLayer =
        new AdapterLayer(getFullQualifiedPackage(basePackage, adaptersPackage), this);
    return this.adapterLayer;
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
        this.domainPackages, Collections.singleton(adapterLayer.getBasePackage()), classes);
    denyAnyDependency(
        this.domainPackages, Collections.singleton(applicationLayer.getBasePackage()), classes);
  }

  public void check(JavaClasses classes) {
    this.adapterLayer.doesNotContainEmptyPackages();
    this.adapterLayer.dontDependOnEachOther(classes);
    this.adapterLayer.doesNotDependOn(this.configurationPackage, classes);
    this.applicationLayer.doesNotContainEmptyPackages();
    this.applicationLayer.doesNotDependOn(this.adapterLayer.getBasePackage(), classes);
    this.applicationLayer.incomingAndOutgoingPortsDoNotDependOnEachOther(classes);
    this.domainDoesNotDependOnOtherPackages(classes);
  }
}
