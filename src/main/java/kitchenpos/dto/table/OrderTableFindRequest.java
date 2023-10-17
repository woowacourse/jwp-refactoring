package kitchenpos.dto.table;

public class OrderTableFindRequest {

    private final Long id;

    public OrderTableFindRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
