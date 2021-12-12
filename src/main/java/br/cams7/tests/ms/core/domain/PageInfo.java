package br.cams7.tests.ms.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageInfo {

  private int pageNumber;
  private int pageSize;
}
