package table.dto;

public class OrderTableRequest {
    private Long id;

    private OrderTableRequest(final Long id) {
        this.id = id;
    }

    public OrderTableRequest() {
    }

    public static OrderTableRequest of(final Long id) {
        return new OrderTableRequest(id);
    }

    public Long getId() {
        return id;
    }
}
