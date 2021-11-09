package kitchenpos.application.dtos;

import kitchenpos.domain.MenuProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductRequest(MenuProduct menuProduct) {
        this(menuProduct.getProductId(), menuProduct.getQuantity());
    }
}
