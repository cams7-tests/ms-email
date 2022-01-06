package br.cams7.tests.ms.core.port.pagination;

import java.util.List;
import lombok.Data;

@Data
public class PageDTO<T> {
  // Page
  /** Returns the number of total pages */
  private int totalPages;
  /** Returns the total amount of elements. */
  private long totalElements;

  // Slice
  /** Returns the number of the current slice */
  private int number;
  /** Returns the number of the current slice */
  private int size;
  /** Returns the number of elements currently on this slice */
  private int numberOfElements;
  /** Returns the page content as list */
  private List<T> content;
  /** Returns whether the slice has content at all */
  private boolean hasContent;
  /** Returns the sorting parameters for the slice */
  private SortDTO sort;
  /** Returns whether the current slice is the first one */
  private boolean first;
  /** Returns whether the current slice is the last one */
  private boolean last;
  /** Returns if there is a next slice */
  private boolean hasNext;
  /** Returns if there is a previous slice */
  private boolean hasPrevious;
}
