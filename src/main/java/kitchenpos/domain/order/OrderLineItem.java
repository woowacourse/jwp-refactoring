package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.base.BaseSeqEntity;
import kitchenpos.domain.menu.Menu;

@Entity
public class OrderLineItem extends BaseSeqEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    protected OrderLineItem(Long seq, Order order, Menu menu, OrderLineItemQuantity quantity) {
        super(seq);
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItem of(Long seq, Menu menu, long quantity) {
        return new OrderLineItem(seq, null, menu, new OrderLineItemQuantity(quantity));
    }

    public static OrderLineItem entityOf(Menu menu, long quantity) {
        return of(null, menu, quantity);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if (Objects.nonNull(this.order)) {
            throw new IllegalArgumentException("OrderLineItem에 이미 Order가 설정되어있습니다.");
        }
        this.order = order;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
            "seq=" + getSeq() +
            ", quantity=" + quantity +
            '}';
    }
}
