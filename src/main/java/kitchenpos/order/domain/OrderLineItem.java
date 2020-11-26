package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    @Builder
    public OrderLineItem(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void setOrderId(final Order order) {
        validateAccessThroughOrder(order);
        this.orderId = order.getId();
    }

    private void validateAccessThroughOrder(final Order order) {
        if (!order.getOrderLineItems().contains(this)) {
            throw new IllegalStateException();
        }
    }
}
