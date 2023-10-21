package kitchenpos.dto.table;

public class SingleOrderTableCreateRequest {

    private final Long id;

    private SingleOrderTableCreateRequest(final Long id) {
        this.id = id;
    }

    public static SingleOrderTableCreateRequest from(final Long id) {
        return new SingleOrderTableCreateRequest(id);
    }

    public Long getId() {
        return id;
    }
}
