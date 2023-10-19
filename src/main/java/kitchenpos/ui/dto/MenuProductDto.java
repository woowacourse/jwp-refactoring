package kitchenpos.ui.dto;

import kitchenpos.domain.menu.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductDto {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductDto(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductDto(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
    }

    public static List<MenuProductDto> of(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());
    }

    public static MenuProductDto from(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity());
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
