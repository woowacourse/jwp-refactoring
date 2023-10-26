package kitchenpos.tablegroup.service.dto;

public class OrderTableIdRequest {

    private long id;

    public OrderTableIdRequest(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
