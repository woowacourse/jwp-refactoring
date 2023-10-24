package kitchenpos.domain;

import org.springframework.data.annotation.Id;

public class OrderLineItem {
    @Id
    private final Long seq;
    private final Menu menu;
    private final long quantity;

    private OrderLineItem(Long seq, Menu menu, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public static final class OrderLineItemBuilder {
        private Long seq;
        private Menu menu;
        private long quantity;

        private OrderLineItemBuilder() {
        }

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public OrderLineItemBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(seq, menu, quantity);
        }
    }
}
