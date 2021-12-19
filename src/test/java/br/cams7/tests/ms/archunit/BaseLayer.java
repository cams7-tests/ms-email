package br.cams7.tests.ms.archunit;

import static br.cams7.tests.ms.archunit.ArchitectureElement.denyDependency;
import static br.cams7.tests.ms.archunit.ArchitectureElement.denyEmptyPackages;

import com.tngtech.archunit.core.domain.JavaClasses;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseLayer {

  private final String basePackage;
  private final HexagonalArchitecture parentContext;

  public HexagonalArchitecture and() {
    return parentContext;
  }

  String getBasePackage() {
    return basePackage;
  }

  public void doesNotDependOn(String packageName, JavaClasses classes) {
    denyDependency(getBasePackage(), packageName, classes);
  }

  void doesNotContainEmptyPackages() {
    denyEmptyPackages(allPackages());
  }

  abstract List<String> allPackages();
}
