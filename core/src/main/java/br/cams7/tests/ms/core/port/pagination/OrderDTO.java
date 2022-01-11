/** */
package br.cams7.tests.ms.core.port.pagination;

import lombok.Data;

@Data
public class OrderDTO {
  // Order
  private DirectionEnum direction;
  private String property;
}
