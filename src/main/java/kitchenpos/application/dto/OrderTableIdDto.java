package kitchenpos.application.dto;

public class OrderTableIdDto {

    private final Long id;

    private OrderTableIdDto(final Long id) {
        this.id = id;
    }

    public static OrderTableIdDto from(final kitchenpos.ui.dto.request.OrderTableIdDto orderTableIdDto) {
        return new OrderTableIdDto(orderTableIdDto.getId());
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
