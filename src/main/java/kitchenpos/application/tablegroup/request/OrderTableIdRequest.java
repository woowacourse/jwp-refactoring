package kitchenpos.application.tablegroup.request;

public class OrderTableIdRequest {

    private Long id;

    public OrderTableIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
