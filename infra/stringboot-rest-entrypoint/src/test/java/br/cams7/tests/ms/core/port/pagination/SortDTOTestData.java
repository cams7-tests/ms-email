package br.cams7.tests.ms.core.port.pagination;

import static br.cams7.tests.ms.core.port.pagination.OrderDTOTestData.getOrderDTO;

import java.util.List;

public class SortDTOTestData {

  public static final OrderDTO ORDER = getOrderDTO();

  public static SortDTO getSortDTO() {
    var sort = new SortDTO();
    sort.setSorted(true);
    sort.setOrders(List.of(ORDER));
    return sort;
  }
}
