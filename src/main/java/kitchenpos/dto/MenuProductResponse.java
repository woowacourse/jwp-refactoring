package kitchenpos.dto;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse(final long seq, final ProductResponse product, final long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                ProductResponse.from(menuProduct.getProduct()),
                menuProduct.getQuantity().getValue()
        );
    }

    public long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
