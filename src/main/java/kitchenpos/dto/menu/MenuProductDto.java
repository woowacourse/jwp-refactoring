package kitchenpos.dto.menu;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductDto {
    private final Long id;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProductDto(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductDto of(MenuProduct menuProduct) {
        return new MenuProductDto(
                menuProduct.getId(),
                menuProduct.getMenu().getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    public static List<MenuProductDto> listOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductDto::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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
