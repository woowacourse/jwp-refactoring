package kitchenpos.application.dto.result;

import kitchenpos.domain.menu.MenuProduct;

public class ProductInMenuResult {

    private final Long seq;
    private final Long quantity;
    private final Long productId;

    public ProductInMenuResult(
            final Long seq,
            final Long quantity,
            final Long productId
    ) {
        this.seq = seq;
        this.quantity = quantity;
        this.productId = productId;
    }

    public static ProductInMenuResult from(final MenuProduct menuProduct) {
        return new ProductInMenuResult(
                menuProduct.getSeq(),
                menuProduct.getQuantity(),
                menuProduct.getProductId()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
