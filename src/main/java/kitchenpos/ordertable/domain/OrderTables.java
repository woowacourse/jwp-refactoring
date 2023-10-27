package kitchenpos.ordertable.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void reset() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateEmpty(false);
            orderTable.updateTableGroup(null);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
