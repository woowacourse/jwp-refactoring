package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.order.exception.InvalidOrderLineItemException;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Embedded
    private OrderLineItemQuantity quantity;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NotNull
    @JoinColumn(name = "menu_id")
    private Long menuId;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long quantity, Order order, Long menuId) {
        this(null, new OrderLineItemQuantity(quantity), order, menuId);
    }

    public OrderLineItem(Long seq, OrderLineItemQuantity quantity, Order order, Long menuId) {
        this.seq = seq;
        this.quantity = quantity;
        this.order = order;
        this.menuId = menuId;
        validateNull(this.order);
        validateNull(this.menuId);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidOrderLineItemException("OrderLineItem 정보에 null이 포함되었습니다.");
        }
    }

    public void updateMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity.getValue();
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menuId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
