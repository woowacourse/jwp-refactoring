package kitchenpos.menu.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductResponse {

    private final long seq;
    private final long menuId;
    private final long productId;
    private final long quantity;

    private MenuProductResponse(final long seq, final long menuId, final long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> from(final List<MenuProduct> menuProducts, final Long menuId) {
        return menuProducts.stream()
                .map(each -> new MenuProductResponse(
                        each.getSeq(),
                        menuId,
                        each.getProductId(),
                        each.getQuantity()
                )).collect(Collectors.toList());
    }

    public long getSeq() {
        return seq;
    }

    public long getMenuId() {
        return menuId;
    }

    public long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
