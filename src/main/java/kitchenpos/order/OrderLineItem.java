package kitchenpos.order;

import kitchenpos.menu.MenuName;
import kitchenpos.menu.MenuPrice;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Embedded
    private MenuName name;
    @Embedded
    private MenuPrice price;
    @Embedded
    private OrderLineItemQuantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(final MenuName name,
                         final MenuPrice price,
                         final OrderLineItemQuantity quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public MenuName getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public OrderLineItemQuantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
