package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {
    private Long productId;
    private int quantity;

    public MenuProductCreateRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(menuId, this.productId, this.quantity);
    }

    public static List<MenuProduct> listOf(List<MenuProductCreateRequest> menuProducts,
        Long menuId) {
        return menuProducts.stream()
            .map(products -> products.toEntity(menuId))
            .collect(Collectors.toList());
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
