package br.cams7.tests.ms.core.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PageDTO {

  private int pageNumber;
  private int pageSize;
}
