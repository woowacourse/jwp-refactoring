package kitchenpos.application.dto.request;

public class OrderTableChangeRequestDto {

    private Long orderTableId;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableChangeRequestDto() {
    }

    public OrderTableChangeRequestDto(Long orderTableId, int numberOfGuests, boolean empty) {
        this.orderTableId = orderTableId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
