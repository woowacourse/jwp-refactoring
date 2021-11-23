package kitchenpos.application.dto.response;

public class OrdersResponseDto {

    private Long id;

    public OrdersResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
