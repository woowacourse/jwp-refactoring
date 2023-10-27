package kitchenpos.ui.response;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final Long quantity;

    private MenuProductResponse(final Long seq, final Long menuId, final Long productId, final Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> from(final List<MenuProduct> menuProducts, final Menu menu) {
        return menuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(
                        menuProduct.getSeq(),
                        menu.getId(),
                        menuProduct.getSeq(),
                        menuProduct.getQuantity()
                )).collect(Collectors.toList());
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
