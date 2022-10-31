package kitchenpos.ui.dto;

public class OrderTableIdDto {

    private Long id;

    private OrderTableIdDto() {
    }

    public OrderTableIdDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
