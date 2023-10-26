package kitchenpos.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getId(),
            menuProduct.getMenu().getId(),
            menuProduct.getProduct().getId(),
            menuProduct.getQuantity()
        );
    }

    public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());
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

    public long getQuantity() {
        return quantity;
    }
}
