package kitchenpos.dto.menu.response;

import kitchenpos.domain.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    private MenuProductResponse(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
