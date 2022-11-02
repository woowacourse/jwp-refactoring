package kitchenpos.dto.tablegroup;

public class OrderTableRequest {

    private Long id;

    private OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
