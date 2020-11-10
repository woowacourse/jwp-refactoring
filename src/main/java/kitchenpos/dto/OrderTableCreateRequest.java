package kitchenpos.dto;

import kitchenpos.domain.OrderTable;

import javax.validation.constraints.NotNull;

public class OrderTableCreateRequest {
    private Long id;

    @NotNull(message = "방문한 손님 수는 반드시 존재해야 합니다!")
    private int numberOfGuests;

    @NotNull(message = "주문 등록 가능 여부는 반드시 존재해야 합니다!")
    private boolean empty;

    public OrderTableCreateRequest() {
    }

    public OrderTableCreateRequest(Long id) {
        this.id = id;
    }

    public OrderTableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTableCreateRequest(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toOrderTable() {
        return new OrderTable(this.numberOfGuests, this.empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
