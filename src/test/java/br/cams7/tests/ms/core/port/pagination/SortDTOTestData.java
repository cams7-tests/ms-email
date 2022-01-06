package br.cams7.tests.ms.core.port.pagination;

import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.defaultOrderDTO;

import java.util.List;

public class SortDTOTestData {

  public static final OrderDTO ORDER = defaultOrderDTO();

  public static SortDTO defaultSortDTO() {
    var sort = new SortDTO();
    sort.setSorted(true);
    sort.setOrders(List.of(ORDER));
    return sort;
  }
}
