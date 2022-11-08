package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductDto {

    private Long productId;
    private long quantity;

    public MenuProductDto() {
    }

    public MenuProductDto(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static List<MenuProductDto> from(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());
    }

    private static MenuProductDto from(final MenuProduct menuProduct) {
        return new MenuProductDto(
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(final Product product) {
        return new MenuProduct(product.getId(), quantity, product.getPrice());
    }
}
