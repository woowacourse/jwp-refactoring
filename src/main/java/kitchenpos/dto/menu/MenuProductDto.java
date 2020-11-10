package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductDto {
    private Long id;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductDto(Long productId, long quantity) {
        this(null, null, productId, quantity);
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

    public boolean equalsProduct(Product product) {
        return productId.equals(product.getId());
    }
}
