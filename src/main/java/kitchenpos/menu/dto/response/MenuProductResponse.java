package kitchenpos.menu.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductResponse(MenuProduct menuProduct) {
        seq = menuProduct.getSeq();
        productId = menuProduct.getProductId();
        quantity = menuProduct.getQuantity();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

}
