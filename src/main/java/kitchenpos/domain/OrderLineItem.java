package kitchenpos.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

//    @NotNull
    private Long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Order order, final Menu menu, final Long quantity) {
        validateQuantity(quantity);
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private OrderLineItem(final Menu menu, final Long quantity) {
        this(null, null, menu, quantity);
    }

    public static OrderLineItem create(final Menu menu, final Long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    public static OrderLineItem create(final Order order, final Menu menu, final Long quantity) {
        return new OrderLineItem(null, order, menu, quantity);
    }

    private void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException("주문 항목의 수량은 null이 될 수 없습니다.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("주문 항목의 수량은 0개 이상이어야 합니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
