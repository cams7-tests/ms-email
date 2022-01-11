/** */
package br.cams7.tests.ms.archunit;

import static com.tngtech.archunit.base.DescribedPredicate.greaterThanOrEqualTo;
import static com.tngtech.archunit.lang.conditions.ArchConditions.containNumberOfElements;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import java.util.Set;

public final class ArchitectureElement {

  static String getFullQualifiedPackage(String basePackage, String relativePackage) {
    return basePackage + "." + relativePackage;
  }

  static void denyDependency(String fromPackageName, String toPackageName, JavaClasses classes) {
    noClasses()
        .that()
        .resideInAPackage(matchAllClassesInPackage(fromPackageName))
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(matchAllClassesInPackage(toPackageName))
        .check(classes);
  }

  private static String matchAllClassesInPackage(String packageName) {
    return packageName + "..";
  }

  static void denyAnyDependency(
      Set<String> fromPackages, Set<String> toPackages, JavaClasses classes) {
    for (String fromPackage : fromPackages) {
      for (String toPackage : toPackages) {
        denyDependency(fromPackage, toPackage, classes);
      }
    }
  }

  static void denyEmptyPackage(String packageName) {
    classes()
        .that()
        .resideInAPackage(matchAllClassesInPackage(packageName))
        .should(containNumberOfElements(greaterThanOrEqualTo(1)))
        .check(classesInPackage(packageName));
  }

  private static JavaClasses classesInPackage(String packageName) {
    return new ClassFileImporter()
        .withImportOption(new DoNotIncludeTests())
        .importPackages(packageName);
  }

  static void denyEmptyPackages(Set<String> packages) {
    for (String packageName : packages) {
      denyEmptyPackage(packageName);
    }
  }
}
