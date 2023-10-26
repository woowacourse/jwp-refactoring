package kitchenpos.dto;

import kitchenpos.menu.domain.MenuProduct;

public class ProductQuantityDto {

    private Long productId;
    private long quantity;

    private ProductQuantityDto() {
    }

    public ProductQuantityDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ProductQuantityDto from(MenuProduct menuProduct) {
        return new ProductQuantityDto(menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
