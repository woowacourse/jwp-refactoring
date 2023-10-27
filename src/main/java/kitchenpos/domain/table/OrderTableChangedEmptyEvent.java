package kitchenpos.domain.table;

public class OrderTableChangedEmptyEvent {

    private Long orderTableId;

    public OrderTableChangedEmptyEvent(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }
}
