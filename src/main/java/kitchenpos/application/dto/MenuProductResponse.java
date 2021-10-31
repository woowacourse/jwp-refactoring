package kitchenpos.application.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private Long quantity;

    public MenuProductResponse(Long seq, ProductResponse product, Long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(), ProductResponse.of(menuProduct.getProduct()), menuProduct.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
