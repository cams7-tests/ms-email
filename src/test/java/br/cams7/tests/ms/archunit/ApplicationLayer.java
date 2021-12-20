/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyAnyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLayer extends BaseLayer {

  private final List<String> incomingPortPackages = new ArrayList<>();
  private final List<String> outgoingPortPackages = new ArrayList<>();
  private final List<String> servicePackages = new ArrayList<>();

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

  List<String> allPackages() {
    List<String> allPackages = new ArrayList<>();
    allPackages.addAll(incomingPortPackages);
    allPackages.addAll(outgoingPortPackages);
    allPackages.addAll(servicePackages);
    return allPackages;
  }
}
