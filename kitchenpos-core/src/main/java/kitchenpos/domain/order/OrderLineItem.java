package kitchenpos.domain.order;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class OrderLineItem {
    @Id
    private final Long seq;
    private final Long menuId;
    private final long quantity;
    private final String name;
    private final BigDecimal price;

    private OrderLineItem(Long seq, Long menuId, long quantity, String name, BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public static final class OrderLineItemBuilder {
        private Long seq;
        private Long menuId;
        private long quantity;
        private String name;
        private BigDecimal price;

        private OrderLineItemBuilder() {
        }

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OrderLineItemBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, menuId, quantity, name, price);
        }
    }
}
