package domain.order_lineitem;

import domain.Menu;
import exception.OrderException;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import support.AggregateReference;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long seq;
    @Embedded
    private final MenuInfo menuInfo;
    private final Long quantity;

    protected OrderLineItem() {
        this.seq = null;
        this.menuInfo = null;
        this.quantity = null;
    }

    public OrderLineItem(
            final AggregateReference<Menu> menuId,
            final Long quantity,
            final MenuInfoGenerator menuInfoGenerator
    ) {
        validateQuantity(quantity);
        this.seq = null;
        this.quantity = quantity;
        this.menuInfo = menuInfoGenerator.generateMenuInfo(menuId);
    }

    private void validateQuantity(final Long quantity) {
        if (Objects.isNull(quantity) || quantity <= 0) {
            throw new OrderException.NoQuantityException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public MenuInfo getMenuInfo() {
        return menuInfo;
    }

    public Long getQuantity() {
        return quantity;
    }
}
