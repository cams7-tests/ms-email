package br.cams7.tests.ms.core.port.pagination;

import static br.cams7.tests.ms.core.port.pagination.SortDTOTestData.getSortDTO;

import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;

public class PageDTOTestData {

  public static final int PAGE_NUMBER = 0;
  public static final int PAGE_SIZE = 5;
  public static final SortDTO SORT = getSortDTO();

  public static PageDTO<EmailEntity> getPageDTO(EmailEntity entity) {
    var page = new PageDTO<EmailEntity>();
    page.setTotalPages(1);
    page.setTotalElements(1);
    page.setNumber(PAGE_NUMBER);
    page.setSize(PAGE_SIZE);
    page.setNumberOfElements(1);
    page.setHasContent(true);
    page.setSort(SORT);
    page.setFirst(true);
    page.setLast(false);
    page.setHasNext(false);
    page.setHasPrevious(false);
    page.setContent(List.of(entity));
    return page;
  }
}
