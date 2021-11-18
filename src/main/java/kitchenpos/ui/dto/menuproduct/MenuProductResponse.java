package kitchenpos.ui.dto.menuproduct;

import kitchenpos.domain.menuproduct.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {

    private Long id;
    private long quantity;

    public MenuProductResponse(Long id, long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getQuantity()
        );
    }

    public static List<MenuProductResponse> toList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public long getQuantity() {
        return quantity;
    }
}
