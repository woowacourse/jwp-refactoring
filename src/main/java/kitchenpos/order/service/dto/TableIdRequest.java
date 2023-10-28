package kitchenpos.order.service.dto;

public class TableIdRequest {

    private long id;

    public TableIdRequest(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
