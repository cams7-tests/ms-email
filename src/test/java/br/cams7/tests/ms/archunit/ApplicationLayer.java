/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyAnyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLayer extends BaseLayer {

  private final List<String> incomingPortsPackages = new ArrayList<>();
  private final List<String> outgoingPortsPackages = new ArrayList<>();
  private final List<String> servicePackages = new ArrayList<>();

  public ApplicationLayer(String basePackage, HexagonalArchitecture parentContext) {
    super(basePackage, parentContext);
  }

  public ApplicationLayer incomingPorts(String packageName) {
    incomingPortsPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public ApplicationLayer outgoingPorts(String packageName) {
    outgoingPortsPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public ApplicationLayer services(String packageName) {
    servicePackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public void incomingAndOutgoingPortsDoNotDependOnEachOther(JavaClasses classes) {
    denyAnyDependency(incomingPortsPackages, outgoingPortsPackages, classes);
    denyAnyDependency(outgoingPortsPackages, incomingPortsPackages, classes);
  }

  List<String> allPackages() {
    List<String> allPackages = new ArrayList<>();
    allPackages.addAll(incomingPortsPackages);
    allPackages.addAll(outgoingPortsPackages);
    allPackages.addAll(servicePackages);
    return allPackages;
  }
}
