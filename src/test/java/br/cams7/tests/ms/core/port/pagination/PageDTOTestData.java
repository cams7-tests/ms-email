package br.cams7.tests.ms.core.port.pagination;

import static br.cams7.tests.ms.core.port.pagination.SortDTOTestData.defaultSortDTO;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;

public class PageDTOTestData {

  public static final int PAGE_NUMBER = 0;
  public static final int PAGE_SIZE = 5;
  public static final SortDTO SORT = defaultSortDTO();

  public static PageDTO<EmailResponseDTO> defaultPageDTO(EmailResponseDTO response) {
    @SuppressWarnings("unchecked")
    var page = (PageDTO<EmailResponseDTO>) getPage();
    page.setContent(List.of(response));
    return page;
  }

  public static PageDTO<EmailEntity> defaultPageDTO(EmailEntity entity) {
    @SuppressWarnings("unchecked")
    var page = (PageDTO<EmailEntity>) getPage();
    page.setContent(List.of(entity));
    return page;
  }

  private static PageDTO<?> getPage() {
    var page = new PageDTO<EmailResponseDTO>();
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
    return page;
  }
}
