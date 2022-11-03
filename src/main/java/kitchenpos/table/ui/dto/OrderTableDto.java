package kitchenpos.table.ui.dto;

public class OrderTableDto {

    private Long id;

    private OrderTableDto() {
    }

    public OrderTableDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
