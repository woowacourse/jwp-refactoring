package kitchenpos.table_group.application.dto;

public class OrderTableCreateRequest {

  private Long id;

  public OrderTableCreateRequest(final Long id) {
    this.id = id;
  }

  public OrderTableCreateRequest() {
  }

  public Long getId() {
    return id;
  }
}
