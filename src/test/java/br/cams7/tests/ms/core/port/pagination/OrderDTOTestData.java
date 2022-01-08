package br.cams7.tests.ms.core.port.pagination;

public class OrderDTOTestData {

  public static final DirectionEnum DIRECTION = DirectionEnum.ASC;
  public static final String PROPERTY = "emailSentDate";

  public static OrderDTO getOrderDTO() {
    var order = new OrderDTO();
    order.setDirection(DIRECTION);
    order.setProperty(PROPERTY);
    return order;
  }
}
