package kitchenpos.table.domain;

import org.hibernate.annotations.BatchSize;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @BatchSize(size = 100)
    @JoinColumn(name = "table_group_id", nullable = true, foreignKey = @ForeignKey(name = "fk_order_table_to_table_group"))
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTableItems;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTableItems) {
        this.orderTableItems = orderTableItems;
    }

    public static OrderTables empty() {
        return new OrderTables(new ArrayList<>());
    }

    public void addOrderTables(final OrderTables requestOrderTables) {
        orderTableItems.addAll(requestOrderTables.orderTableItems);
    }

    public void assignTableGroup(final Long tableGroupId) {
        orderTableItems.forEach(orderTable -> orderTable.assignTableGroup(tableGroupId));
        orderTableItems.forEach(orderTable -> orderTable.changeOrderTableEmpty(false));
    }

    public List<OrderTable> getOrderTableItems() {
        return orderTableItems;
    }
}
