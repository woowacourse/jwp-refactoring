package kitchenpos.order.application.dto;

import java.math.BigDecimal;
import java.util.List;

public final class OrderCreateRequest {

    private final Long orderTableId;
    private final List<MenuSnapShot> menuSnapShots;

    public OrderCreateRequest(final Long orderTableId, final List<MenuSnapShot> menuSnapShots) {
        this.orderTableId = orderTableId;
        this.menuSnapShots = menuSnapShots;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<MenuSnapShot> getMenuSnapShots() {
        return menuSnapShots;
    }

    public static class MenuSnapShot {

        private final Long menuId;
        private final String name;
        private final BigDecimal price;
        private final List<ProductSnapShot> productSnapShots;
        private final Long quantity;

        public MenuSnapShot(final Long menuId, final String name, final BigDecimal price, List<ProductSnapShot> productSnapShots, final Long quantity) {
            this.menuId = menuId;
            this.name = name;
            this.price = price;
            this.productSnapShots = productSnapShots;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public List<ProductSnapShot> getProductSnapShots() {
            return productSnapShots;
        }

        public static class ProductSnapShot{
            private final Long menuProductId;
            private final Long productId;
            private final String name;
            private final BigDecimal price;
            private final Long quantity;

            public ProductSnapShot(final Long menuProductId, final Long productId, final String name, final BigDecimal price, final Long quantity) {
                this.menuProductId = menuProductId;
                this.productId = productId;
                this.name = name;
                this.price = price;
                this.quantity = quantity;
            }

            public Long getMenuProductId() {
                return menuProductId;
            }

            public Long getProductId() {
                return productId;
            }

            public String getName() {
                return name;
            }

            public BigDecimal getPrice() {
                return price;
            }

            public Long getQuantity() {
                return quantity;
            }
        }
    }
}
