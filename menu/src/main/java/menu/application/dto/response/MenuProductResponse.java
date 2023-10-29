package menu.application.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import menu.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;

    private final Long menuId;

    private final Long productId;

    private final long quantity;

    private MenuProductResponse(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(MenuProduct menuProduct, Long menuId) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuId,
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    public static List<MenuProductResponse> from(List<MenuProduct> menuProducts, Long menuId) {
        return menuProducts.stream()
                .map(it -> from(it, menuId))
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
