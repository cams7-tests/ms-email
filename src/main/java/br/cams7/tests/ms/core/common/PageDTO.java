package br.cams7.tests.ms.core.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PageDTO {

  private int pageNumber;
  private int pageSize;
}
