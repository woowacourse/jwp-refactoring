package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductDto {
    private Long productId;
    private Long quantity;

    private MenuProductDto(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductDto> listOf(List<MenuProduct> menuProductDtos) {
        return menuProductDtos.stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());
    }

    public static MenuProductDto from(MenuProduct menuProduct) {
        return new MenuProductDto(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
