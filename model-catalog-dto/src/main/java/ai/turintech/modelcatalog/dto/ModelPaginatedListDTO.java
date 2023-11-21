package ai.turintech.modelcatalog.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelPaginatedListDTO {
  private List<ModelDTO> docs = new ArrayList<>();

  private long totalDocs;

  private int limit;

  private int totalPages;

  private int page;

  private int pagingCounter;

  private Boolean hasPrevPage;

  private Boolean hasNextPage;

  private Integer prevPage;
  private Integer nextPage;

  public ModelPaginatedListDTO() {}

  public ModelPaginatedListDTO(
      List<ModelDTO> docs,
      long totalDocs,
      int limit,
      int totalPages,
      int page,
      int pagingCounter,
      Boolean hasPrevPage,
      Boolean hasNextPage,
      Integer prevPage,
      Integer nextPage) {
    this.docs = docs;
    this.totalDocs = totalDocs;
    this.limit = limit;
    this.totalPages = totalPages;
    this.page = page;
    this.pagingCounter = pagingCounter;
    this.hasPrevPage = hasPrevPage;
    this.hasNextPage = hasNextPage;
    this.prevPage = prevPage;
    this.nextPage = nextPage;
  }

  public List<ModelDTO> getDocs() {
    return docs;
  }

  public void setDocs(List<ModelDTO> docs) {
    this.docs = docs;
  }

  public long getTotalDocs() {
    return totalDocs;
  }

  public void setTotalDocs(long totalDocs) {
    this.totalDocs = totalDocs;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPagingCounter() {
    return pagingCounter;
  }

  public void setPagingCounter(int pagingCounter) {
    this.pagingCounter = pagingCounter;
  }

  public Boolean getHasPrevPage() {
    return hasPrevPage;
  }

  public void setHasPrevPage(Boolean hasPrevPage) {
    this.hasPrevPage = hasPrevPage;
  }

  public Boolean getHasNextPage() {
    return hasNextPage;
  }

  public void setHasNextPage(Boolean hasNextPage) {
    this.hasNextPage = hasNextPage;
  }

  public Integer getPrevPage() {
    return prevPage;
  }

  public void setPrevPage(Integer prevPage) {
    this.prevPage = prevPage;
  }

  public Integer getNextPage() {
    return nextPage;
  }

  public void setNextPage(Integer nextPage) {
    this.nextPage = nextPage;
  }

  @Override
  public String toString() {
    return "ModelPaginatedListDTO{"
        + "docs="
        + docs
        + ", totalDocs="
        + totalDocs
        + ", limit="
        + limit
        + ", totalPages="
        + totalPages
        + ", page="
        + page
        + ", pagingCounter="
        + pagingCounter
        + ", hasPrevPage="
        + hasPrevPage
        + ", hasNextPage="
        + hasNextPage
        + ", prevPage="
        + prevPage
        + ", nextPage="
        + nextPage
        + '}';
  }
}
