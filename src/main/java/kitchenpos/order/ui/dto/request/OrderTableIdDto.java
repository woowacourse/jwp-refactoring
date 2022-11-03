package kitchenpos.order.ui.dto.request;

public class OrderTableIdDto {

    private Long id;

    private OrderTableIdDto() {
    }

    public OrderTableIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OrderTableIdDto{" +
                "id=" + id +
                '}';
    }
}
