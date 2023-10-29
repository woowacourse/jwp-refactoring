package kitchenpos.domain.orderlineitem;

import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuPrice;
import kitchenpos.domain.order.Order;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @NotNull
    @Embedded
    private MenuName menuName;
    @NotNull
    @Embedded
    private MenuPrice menuPrice;
    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final Order order, final MenuName menuName, final MenuPrice menuPrice, final OrderLineItemQuantity quantity) {
        this.order = order;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public MenuName getMenuName() {
        return menuName;
    }

    public MenuPrice getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
