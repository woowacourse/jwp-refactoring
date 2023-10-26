package kitchenpos.ui.response;

public class OrderLineItemDtoInOrderResponse {

    private Long id;

    public OrderLineItemDtoInOrderResponse() {
    }

    public OrderLineItemDtoInOrderResponse(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
