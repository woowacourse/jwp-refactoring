package kitchenpos.table.application.dto;

public class OrderTableNumberOfGuestModifyRequest {

  private int numberOfGuests;

  public OrderTableNumberOfGuestModifyRequest(final int numberOfGuests) {
    this.numberOfGuests = numberOfGuests;
  }

  public OrderTableNumberOfGuestModifyRequest() {
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }
}
