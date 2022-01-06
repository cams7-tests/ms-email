package br.cams7.tests.ms.core.port.pagination;

import java.util.List;
import lombok.Data;

@Data
public class SortDTO {
  // Sort
  private boolean sorted;
  private List<OrderDTO> orders;
}
