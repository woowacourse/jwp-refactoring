package kitchenpos.tablegroup.dto;

public class OrderTableRequest {

    private Long id;

    public OrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
