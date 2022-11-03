package kitchenpos.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(Long menuId, MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuId, menuProduct.getProductId(), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> from(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(mp -> from(menu.getId(), mp))
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
