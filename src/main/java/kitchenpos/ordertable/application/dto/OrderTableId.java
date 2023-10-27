package kitchenpos.ordertable.application.dto;

public class OrderTableId {
    private Long id;

    private OrderTableId() {
    }

    public OrderTableId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}