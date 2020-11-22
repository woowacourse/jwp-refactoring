package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.exception.InvalidOrderLineItemQuantityException;
import kitchenpos.util.ValidateUtil;

import javax.persistence.*;

@Entity
public class OrderLineItem {
    private static final long MIN_QUANTITY = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private long quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Order order, Menu menu, long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Order order, Menu menu, long quantity) {
        ValidateUtil.validateNonNull(order, menu);
        if (quantity < MIN_QUANTITY) {
            throw new InvalidOrderLineItemQuantityException("주문 항목의 수량은 " + MIN_QUANTITY + "개 이상이어야 합니다!");
        }

        return new OrderLineItem(order, menu, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Order getOrder() {
        return order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }
}
