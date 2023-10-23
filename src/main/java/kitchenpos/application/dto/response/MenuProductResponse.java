package kitchenpos.application.dto.response;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
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

    public static List<MenuProductResponse> from(Menu menu) {
        return menu.getMenuProducts().stream()
                .map(product -> MenuProductResponse.from(menu, product))
                .collect(Collectors.toList());
    }

    private static MenuProductResponse from(Menu menu, MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menu.getId(), menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public static MenuProductResponseBuilder builder() {
        return new MenuProductResponseBuilder();
    }

    public static final class MenuProductResponseBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        private MenuProductResponseBuilder() {
        }

        public MenuProductResponseBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductResponseBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductResponseBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductResponseBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProductResponse build() {
            return new MenuProductResponse(seq, menuId, productId, quantity);
        }
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
