package kitchenpos.domain;

import javax.persistence.*;

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
    private long quantity;

    public OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final Order order, final Menu menu, final long quantity) {
        validateQuantity(quantity);
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    private OrderLineItem(final Menu menu, final long quantity) {
        this(null, null, menu, quantity);
    }

    public static OrderLineItem create(final Menu menu, final long quantity) {
        return new OrderLineItem(menu, quantity);
    }

    private void validateQuantity(final long quantity) {
        // TODO: OrderLineItem readme 작성하기 (validate)
        if (quantity < 0) {
            throw new IllegalArgumentException("주문 항목의 수량은 0개 이상이어야 합니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return order.getId();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setMenuId(final Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
