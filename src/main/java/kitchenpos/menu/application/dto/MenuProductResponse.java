package kitchenpos.menu.application.dto;

public class MenuProductResponse {

    private final Long productId;
    private final long quantity;

    public MenuProductResponse(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
