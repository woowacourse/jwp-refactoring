package kitchenpos.dto.table;

public class OrderTableRequest {
    private final Long id;

    private OrderTableRequest(final Long id) {
        this.id = id;
    }

    public static OrderTableRequest of(final Long id) {
        return new OrderTableRequest(id);
    }

    public Long getId() {
        return id;
    }
}
