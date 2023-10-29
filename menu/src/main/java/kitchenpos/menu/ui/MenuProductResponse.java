package kitchenpos.menu.ui;

import kitchenpos.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductResponse(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public static MenuProductResponse from(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(),
                menuProduct.getProductId(),
                menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
