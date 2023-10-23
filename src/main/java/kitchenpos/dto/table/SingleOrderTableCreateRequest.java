package kitchenpos.dto.table;

public class SingleOrderTableCreateRequest {

    private final Long id;

    public SingleOrderTableCreateRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
