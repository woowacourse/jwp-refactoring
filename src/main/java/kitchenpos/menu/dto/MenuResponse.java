package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    protected MenuResponse() {
    }

    public MenuResponse(final Long id, final String name, final BigDecimal price,
                        final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static List<MenuResponse> listFrom(final List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
            menu.getId(),
            menu.getName().getValue(),
            menu.getPrice().getValue(),
            menu.getMenuGroupId(),
            MenuProductResponse.listFrom(menu)
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

        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        protected MenuProductResponse() {
        }

        public MenuProductResponse(final Long seq, final Long menuId, final Long productId,
                                   final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static List<MenuProductResponse> listFrom(final Menu menu) {
            return menu.getMenuProducts().getElements()
                .stream()
                .map(menuProduct -> MenuProductResponse.from(menu, menuProduct))
                .collect(Collectors.toList());
        }

        public static MenuProductResponse from(final Menu menu, final MenuProduct menuProduct) {
            return new MenuProductResponse(
                menuProduct.getSeq(),
                menu.getId(),
                menuProduct.getProductId(),
                menuProduct.getQuantity().getValue()
            );
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
