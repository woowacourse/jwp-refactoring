package kitchenpos.application.dto.menu;

import kitchenpos.domain.MenuProduct;

public class ProductQuantityDto {

    private Long productId;
    private long quantity;

    private ProductQuantityDto() {}

    public ProductQuantityDto(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ProductQuantityDto from(final MenuProduct menuProduct) {
        return new ProductQuantityDto(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
