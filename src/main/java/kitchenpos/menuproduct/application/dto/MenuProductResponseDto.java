package kitchenpos.menuproduct.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menuproduct.model.MenuProduct;

public class MenuProductResponseDto {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private static MenuProductResponseDto from(MenuProduct menuProduct) {
        return new MenuProductResponseDto(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
            menuProduct.getQuantity());
    }

    public static List<MenuProductResponseDto> listOf(List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
            .map(MenuProductResponseDto::from)
            .collect(Collectors.toList());
    }

    public MenuProductResponseDto(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
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
