package kitchenpos.dto.response;

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

    protected MenuResponse() {
    }

    public MenuResponse(
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

    public static MenuResponse from(final Menu menu) {
        return new MenuResponse(
            menu.getId(),
            menu.getName(),
            menu.getPrice().getValue(),
            menu.getMenuGroup().getId(),
            MenuProductResponse.from(menu)
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

        public static List<MenuProductResponse> from(final Menu menu) {
            return menu.getMenuProducts().getElements()
                .stream()
                .map(MenuProductResponse::from)
                .collect(Collectors.toList());
        }

        public static MenuProductResponse from(final MenuProduct menuProduct) {
            return new MenuProductResponse(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
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
