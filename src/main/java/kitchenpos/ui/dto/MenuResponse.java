package kitchenpos.ui.dto;

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
    private List<MenuInnerMenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    public MenuResponse(final Long id,
                        final String name,
                        final BigDecimal price,
                        final Long menuGroupId,
                        final List<MenuInnerMenuProductResponse> menuProducts) {
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
                menu.getPrice(),
                menu.getMenuGroupId(),
                mapToInnerMenuProductResponse(menu.getMenuProducts())
        );
    }

    private static List<MenuInnerMenuProductResponse> mapToInnerMenuProductResponse(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuInnerMenuProductResponse::from)
                .collect(Collectors.toList());
    }

    public static class MenuInnerMenuProductResponse {

        private Long seq;
        private Long menuUd;
        private Long productId;
        private long quantity;

        private MenuInnerMenuProductResponse() {
        }

        public MenuInnerMenuProductResponse(final Long seq,
                                            final Long menuUd,
                                            final Long productId,
                                            final long quantity) {
            this.seq = seq;
            this.menuUd = menuUd;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuInnerMenuProductResponse from(final MenuProduct menuProduct) {
            return new MenuInnerMenuProductResponse(
                    menuProduct.getSeq(),
                    menuProduct.getMenuId(),
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
            );
        }

        public Long getSeq() {
            return seq;
        }

        public Long getMenuUd() {
            return menuUd;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
