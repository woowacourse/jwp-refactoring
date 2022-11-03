package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuInnerMenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(final String name,
                       final BigDecimal price,
                       final Long menuGroupId,
                       final List<MenuInnerMenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuInnerMenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuInnerMenuProductRequest{

        private Long productId;
        private long quantity;

        private MenuInnerMenuProductRequest() {
        }

        public MenuInnerMenuProductRequest(final Long productId, final long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
