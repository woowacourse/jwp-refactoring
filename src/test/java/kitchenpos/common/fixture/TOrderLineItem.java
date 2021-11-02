package kitchenpos.common.fixture;

import kitchenpos.domain.OrderLineItem;

public class TOrderLineItem {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long seq;
        private Long orderId;
        private Long menuId;
        private Long quantity;

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderLineItem build() {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setSeq(seq);
            orderLineItem.setOrderId(orderId);
            orderLineItem.setMenuId(menuId);
            orderLineItem.setQuantity(quantity);
            return orderLineItem;
        }
    }
}
