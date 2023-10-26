package kitchenpos.table.dto;

public class SingleOrderTableCreateRequest {

    private Long id;

    private SingleOrderTableCreateRequest() {
    }

    public SingleOrderTableCreateRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
