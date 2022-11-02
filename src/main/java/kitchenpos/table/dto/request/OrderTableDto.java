package kitchenpos.table.dto.request;

public class OrderTableDto {

    private Long id;

    public OrderTableDto() {
    }

    public OrderTableDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
