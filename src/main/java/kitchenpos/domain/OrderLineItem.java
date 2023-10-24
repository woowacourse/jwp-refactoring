package kitchenpos.domain;

import org.springframework.data.annotation.Id;

public class OrderLineItem {
    @Id
    private final Long seq;
    private final Long menuId;
    private final long quantity;

    private OrderLineItem(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
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

        public OrderLineItem build() {
            return new OrderLineItem(seq, menuId, quantity);
        }
    }
}
