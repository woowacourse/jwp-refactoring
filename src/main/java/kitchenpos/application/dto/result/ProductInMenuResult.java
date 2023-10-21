package kitchenpos.application.dto.result;

import kitchenpos.domain.menu.MenuProduct;

public class ProductInMenuResult {

    private final Long seq;
    private final Long quantity;
    private final ProductResult productResult;

    public ProductInMenuResult(
            final Long seq,
            final Long quantity,
            final ProductResult productResult
    ) {
        this.seq = seq;
        this.quantity = quantity;
        this.productResult = productResult;
    }

    public static ProductInMenuResult from(final MenuProduct menuProduct) {
        return new ProductInMenuResult(
                menuProduct.getSeq(),
                menuProduct.getQuantity(),
                ProductResult.from(menuProduct.getProduct())
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public ProductResult getProductResult() {
        return productResult;
    }
}
