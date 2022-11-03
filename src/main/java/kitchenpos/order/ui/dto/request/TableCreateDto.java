package kitchenpos.order.ui.dto.request;

public class TableCreateDto {

    private Long id;

    public TableCreateDto() {
    }

    public TableCreateDto(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
