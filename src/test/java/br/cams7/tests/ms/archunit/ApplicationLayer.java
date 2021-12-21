/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyAnyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.HashSet;
import java.util.Set;

public class ApplicationLayer extends BaseLayer {

  private final Set<String> incomingPortPackages = new HashSet<>();
  private final Set<String> outgoingPortPackages = new HashSet<>();
  private final Set<String> servicePackages = new HashSet<>();

  ApplicationLayer(String basePackage, HexagonalArchitecture parentContext) {
    super(basePackage, parentContext);
  }

  public ApplicationLayer incomingPorts(String packageName) {
    incomingPortPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public ApplicationLayer outgoingPorts(String packageName) {
    outgoingPortPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public ApplicationLayer services(String packageName) {
    servicePackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  void incomingAndOutgoingPortsDoNotDependOnEachOther(JavaClasses classes) {
    denyAnyDependency(incomingPortPackages, outgoingPortPackages, classes);
    denyAnyDependency(outgoingPortPackages, incomingPortPackages, classes);
  }

  Set<String> allPackages() {
    Set<String> allPackages = new HashSet<>();
    allPackages.addAll(incomingPortPackages);
    allPackages.addAll(outgoingPortPackages);
    allPackages.addAll(servicePackages);
    return allPackages;
  }
}
