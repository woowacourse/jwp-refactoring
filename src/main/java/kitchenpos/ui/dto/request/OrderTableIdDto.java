package kitchenpos.ui.dto.request;

public class OrderTableIdDto {

    private Long id;

    private OrderTableIdDto() {}

    public OrderTableIdDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
