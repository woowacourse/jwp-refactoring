package kitchenpos.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), ProductResponse.from(menuProduct.getProduct()),
                menuProduct.getQuantity());
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
