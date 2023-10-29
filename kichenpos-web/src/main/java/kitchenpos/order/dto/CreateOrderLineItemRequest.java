package kitchenpos.order.dto;

public class CreateOrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public CreateOrderLineItemRequest() {
    }

    public CreateOrderLineItemDto toCreateOrderLineItemDto() {
        return new CreateOrderLineItemDto(menuId, quantity);
    }
}
