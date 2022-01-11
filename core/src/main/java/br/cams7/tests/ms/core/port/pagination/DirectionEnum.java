/** */
package br.cams7.tests.ms.core.port.pagination;

/** @author CIANDT\cmagalhaes */
public enum DirectionEnum {
  // Direction
  ASC,
  DESC;

  public boolean isAscending() {
    return this.equals(ASC);
  }

  public boolean isDescending() {
    return this.equals(DESC);
  }
}
