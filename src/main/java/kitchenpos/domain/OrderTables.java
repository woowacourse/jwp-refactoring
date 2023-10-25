package kitchenpos.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    protected OrderTables() {
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
