package kitchenpos.table.ui.dto;

public class OrderTableRequestDto {

    private Long id;

    private OrderTableRequestDto() {
    }

    public OrderTableRequestDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
