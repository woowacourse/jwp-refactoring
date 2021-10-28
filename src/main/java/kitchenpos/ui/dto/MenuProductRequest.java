package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    private MenuProductRequest() {
    }

    private MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest from(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

//    public MenuProduct toMenuProduct() {
//        return new MenuProduct(
//                productId,
//                quantity
//        );
//    }
}
