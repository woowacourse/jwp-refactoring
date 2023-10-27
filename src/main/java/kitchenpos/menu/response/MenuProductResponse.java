package kitchenpos.menu.response;

import kitchenpos.menu.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<MenuProductResponse> from(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(
                        menuProduct.getSeq(),
                        menuProduct.getMenuId(),
                        menuProduct.getProductId(),
                        menuProduct.getQuantity()
                ))
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
