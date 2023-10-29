package kitchenpos.menu.application.response;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroup menuGroup;
    private final List<MenuProductResponse> menuProducts;

    private MenuResponse(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup(),
                menu.getMenuProducts().stream().map(MenuProductResponse::from).collect(Collectors.toList()));
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductResponse {
        private final Long seq;
        private final Long menuId;
        private final Long productId;
        private final long quantity;

        private MenuProductResponse(Long seq, Long menuId, Long productId, long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse from(MenuProduct menuProduct) {
            return new MenuProductResponse(menuProduct.getSeq(),
                    menuProduct.getMenu().getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
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
}
