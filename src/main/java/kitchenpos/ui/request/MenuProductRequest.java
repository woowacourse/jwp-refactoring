package kitchenpos.ui.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long productId;
    private long quantity;

    public static MenuProductRequest create(Long productId, long quantity) {
        final MenuProductRequest menuProductRequest = new MenuProductRequest();
        menuProductRequest.productId = productId;
        menuProductRequest.quantity = quantity;
        return menuProductRequest;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity() {
        return MenuProduct.create(productId, quantity);
    }
}
