package kitchenpos.table.request;

public class OrderTableDto {

    private Long id;

    public OrderTableDto() {
    }

    public OrderTableDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
