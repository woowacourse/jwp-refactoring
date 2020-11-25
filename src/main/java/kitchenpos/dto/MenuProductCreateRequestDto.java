package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequestDto {
    private Long productId;
    private long quantity;

    private MenuProductCreateRequestDto() {
    }

    public MenuProductCreateRequestDto(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(null, menuId, productId, quantity);
    }
}
