package kitchenpos.menu.application.dto;

public class MenuProductRequest {
    private Long productId;
    private Integer quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(final Long productId, final Integer quantity) {
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
