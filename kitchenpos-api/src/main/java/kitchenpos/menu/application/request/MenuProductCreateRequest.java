package kitchenpos.menu.application.request;

public class MenuProductCreateRequest {

    private Long productId;
    private Integer quantity;

    public MenuProductCreateRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
