package kitchenpos.tablegroup.request;

public class OrderTableDto {

    private final Long id;

    public OrderTableDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
