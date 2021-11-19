package kitchenpos.menu.ui.response;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.ui.response.ProductResponse;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                ProductResponse.from(menuProduct.getProduct()),
                menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
