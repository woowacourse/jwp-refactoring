package kitchenpos.application.support.domain;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Order order = null;
        private Menu menu = MenuTestSupport.builder().build();
        private long quantity = 2;

        public Builder Order(final Order order) {
            this.order = order;
            return this;
        }

        public Builder Menu(final Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder Quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            return new OrderLineItem(menu, quantity);
        }
    }
}
