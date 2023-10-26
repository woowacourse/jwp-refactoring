package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateCanBeGrouped() {
        if (orderTables.stream().anyMatch(savedOrderTable -> !savedOrderTable.isEmpty())) {
            throw new IllegalArgumentException();
        }

        boolean isExistGroupedOrderTable = orderTables.stream().anyMatch(OrderTable::isGrouped);

        if (isExistGroupedOrderTable) {
            throw new IllegalArgumentException();
        }
    }

    public void add(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public void setTableGroup(TableGroup tableGroup) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.setTableGroup(tableGroup);
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
