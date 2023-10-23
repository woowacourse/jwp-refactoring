package kitchenpos.ui.dto.request;

public class CreateOrderGroupOrderTableRequest {

    private Long id;

    public CreateOrderGroupOrderTableRequest() {
    }

    public CreateOrderGroupOrderTableRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
