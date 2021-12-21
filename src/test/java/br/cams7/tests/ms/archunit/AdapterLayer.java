/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.HashSet;
import java.util.Set;

public class AdapterLayer extends BaseLayer {

  private final Set<String> incomingAdapterPackages = new HashSet<>();
  private final Set<String> outgoingAdapterPackages = new HashSet<>();

  AdapterLayer(String basePackage, HexagonalArchitecture parentContext) {
    super(basePackage, parentContext);
  }

  public AdapterLayer outgoing(String packageName) {
    outgoingAdapterPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public AdapterLayer incoming(String packageName) {
    incomingAdapterPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  Set<String> allPackages() {
    Set<String> allAdapters = new HashSet<>();
    allAdapters.addAll(incomingAdapterPackages);
    allAdapters.addAll(outgoingAdapterPackages);
    return allAdapters;
  }

  void dontDependOnEachOther(JavaClasses classes) {
    Set<String> allAdapters = allPackages();
    for (String adapter1 : allAdapters) {
      for (String adapter2 : allAdapters) {
        if (!adapter1.equals(adapter2)) {
          denyDependency(adapter1, adapter2, classes);
        }
      }
    }
  }
}
