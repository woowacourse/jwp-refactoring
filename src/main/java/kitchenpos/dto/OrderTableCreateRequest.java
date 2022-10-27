package kitchenpos.dto;

public class OrderTableCreateRequest {

    private Long id;

    public OrderTableCreateRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
