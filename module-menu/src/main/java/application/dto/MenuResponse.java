package application.dto;

import domain.Menu;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import domain.menu_product.MenuProduct;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(
            final Long id,
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductResponse> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getMenuName().getName(),
                menu.getMenuPrice().getPrice(),
                menu.getMenuGroup().getId(),
                menu.getMenuProducts().menuProducts().stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductResponse {

        private final Long seq;
        private final Long productId;
        private final Long quantity;

        private MenuProductResponse(final Long seq, final Long productId, final Long quantity) {
            this.seq = seq;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse from(final MenuProduct menuProduct) {
            return new MenuProductResponse(
                    menuProduct.getSeq(),
                    menuProduct.getProductId().getId(),
                    menuProduct.getQuantity()
            );
        }

        public Long getSeq() {
            return seq;
        }

        public Long getProductId() {
            return productId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
