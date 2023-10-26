package kitchenpos.domain.order.order_lineitem;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.domain.menu.Menu;
import kitchenpos.support.AggregateReference;
import kitchenpos.exception.OrderException;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "menu_id"))
    private final AggregateReference<Menu> menu;
    private final Long quantity;

    protected OrderLineItem() {
        this.seq = null;
        this.menu = null;
        this.quantity = null;
    }

    public OrderLineItem(
            final AggregateReference<Menu> menu,
            final Long quantity,
            final OrderLineValidator orderLineValidator
    ) {
        validateQuantity(quantity);
        this.seq = null;
        this.menu = menu;
        this.quantity = quantity;
        orderLineValidator.validate(this);
    }

    private void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity) || quantity <= 0) {
            throw new OrderException.NoQuantityException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public AggregateReference<Menu> getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
