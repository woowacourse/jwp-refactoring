package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.menu.Menu;
import kitchenpos.exception.OrderException;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private final Menu menu;
    private final Long quantity;

    protected OrderLineItem() {
        this.seq = null;
        this.menu = null;
        this.quantity = null;
    }

    public OrderLineItem(final Menu menu, final Long quantity) {
        validateMenu(menu);
        validateQuantity(quantity);
        this.seq = null;
        this.menu = menu;
        this.quantity = quantity;
    }

    private void validateMenu(final Menu menu) {
        if (Objects.isNull(menu)) {
            throw new OrderException.NoMenuException();
        }
    }

    private void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity) || quantity <= 0) {
            throw new OrderException.NoQuantityException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
