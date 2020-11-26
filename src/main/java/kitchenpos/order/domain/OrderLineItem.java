package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private long quantity;

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public OrderLineItemBuilder toBuilder() {
        return new OrderLineItemBuilder(seq, orderId, menuId, quantity);
    }

    public static class OrderLineItemBuilder {
        private Long seq;
        private Long orderId;
        private Long menuId;
        private long quantity;

        public OrderLineItemBuilder() {
        }

        public OrderLineItemBuilder(Long seq, Long orderId, Long menuId, long quantity) {
            this.seq = seq;
            this.orderId = orderId;
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public OrderLineItemBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public OrderLineItemBuilder orderId(Long orderId) {
            this.orderId = orderId;
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
            return new OrderLineItem(seq, orderId, menuId, quantity);
        }
    }
}
