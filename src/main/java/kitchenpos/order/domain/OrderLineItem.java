package kitchenpos.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.menu.domain.UpdatableMenuInfo;
import kitchenpos.product.domain.Price;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private UpdatableMenuInfo updatableMenuInfo;
    private long quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(final Long seq, final UpdatableMenuInfo updatableMenuInfo, final long quantity) {
        this.seq = seq;
        this.updatableMenuInfo = updatableMenuInfo;
        this.quantity = quantity;
    }

    public OrderLineItem(final UpdatableMenuInfo updatableMenuInfo, final long quantity) {
        this(null, updatableMenuInfo, quantity);
    }

    public OrderLineItem(final String name, final Price price, final long quantity) {
        this(null, new UpdatableMenuInfo(price, name), quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return updatableMenuInfo.getName();
    }

    public Price getPrice() {
        return updatableMenuInfo.getPrice();
    }

    public long getQuantity() {
        return quantity;
    }
}
