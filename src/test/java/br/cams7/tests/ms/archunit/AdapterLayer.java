/** */
package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.getFullQualifiedPackage;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.ArrayList;
import java.util.List;

public class AdapterLayer extends BaseLayer {

  private final List<String> incomingAdapterPackages = new ArrayList<>();
  private final List<String> outgoingAdapterPackages = new ArrayList<>();

  AdapterLayer(String basePackage, HexagonalArchitecture parentContext) {
    super(basePackage, parentContext);
  }

  public AdapterLayer outgoing(String packageName) {
    incomingAdapterPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  public AdapterLayer incoming(String packageName) {
    outgoingAdapterPackages.add(getFullQualifiedPackage(getBasePackage(), packageName));
    return this;
  }

  List<String> allPackages() {
    List<String> allAdapters = new ArrayList<>();
    allAdapters.addAll(incomingAdapterPackages);
    allAdapters.addAll(outgoingAdapterPackages);
    return allAdapters;
  }

  void dontDependOnEachOther(JavaClasses classes) {
    List<String> allAdapters = allPackages();
    for (String adapter1 : allAdapters) {
      for (String adapter2 : allAdapters) {
        if (!adapter1.equals(adapter2)) {
          denyDependency(adapter1, adapter2, classes);
        }
      }
    }
  }
}
