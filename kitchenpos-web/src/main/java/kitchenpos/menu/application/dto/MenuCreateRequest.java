package kitchenpos.menu.application.dto;

import java.math.BigDecimal;
import java.util.List;

public final class MenuCreateRequest {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreate> menuProductCreates;

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductCreate> menuProductCreates) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCreates = menuProductCreates;
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

    public List<MenuProductCreate> getMenuProductCreates() {
        return menuProductCreates;
    }

    public static class MenuProductCreate {
        private final Long productId;
        private final Long quantity;

        public MenuProductCreate(final Long productId, final Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
