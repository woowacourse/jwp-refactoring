package kitchenpos.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    @NotNull(message = "상품명을 입력해주세요.")
    private final Long productId;
    @Min(value = 1, message = "최소 수량이 1 이상이 되어야 합니다.")
    private final Long quantity;

    public MenuProductRequest(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
