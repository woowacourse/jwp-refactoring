package kitchenpos.application.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
                         List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(
                menu.getId(),
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                MenuProductResponse.listOf(menu.getMenuProducts())
        );
    }

    public static List<MenuResponse> listOf(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
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
        private Long productId;
        private long quantity;

        private MenuProductResponse(Long productId, long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse of(MenuProduct menuProduct) {
            return new MenuProductResponse(
                    menuProduct.getProduct().getId(),
                    menuProduct.getQuantity()
            );
        }

        public static List<MenuProductResponse> listOf(List<MenuProduct> menuProducts) {
            return menuProducts.stream().map(MenuProductResponse::of).collect(Collectors.toList());
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
