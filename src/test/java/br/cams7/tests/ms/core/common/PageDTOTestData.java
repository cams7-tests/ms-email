package br.cams7.tests.ms.core.common;

public class PageDTOTestData {
  public static int PAGE_NUMBER = 0;
  public static int PAGE_SIZE = 10;

  public static PageDTO defaultPage() {
    var page = new PageDTO();
    page.setPageNumber(PAGE_NUMBER);
    page.setPageSize(PAGE_SIZE);
    return page;
  }
}
