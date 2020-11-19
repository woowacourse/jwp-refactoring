package kitchenpos.dto;

public class OrderTableRequest {
    private Long id;

    protected OrderTableRequest() {

    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
