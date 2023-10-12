package kitchenpos.application.support.domain;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static Long autoCount = 0L;

        private Long seq = ++autoCount;
        private Long orderId = null;
        private Long menuId = MenuTestSupport.builder().build().getId();
        private long quantity = 2;

        public Builder Seq(final Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder OrderId(final Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder MenuId(final Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder Quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            final var result = new OrderLineItem();
            result.setSeq(seq);
            result.setOrderId(orderId);
            result.setMenuId(menuId);
            result.setQuantity(quantity);
            return result;
        }
    }
}
