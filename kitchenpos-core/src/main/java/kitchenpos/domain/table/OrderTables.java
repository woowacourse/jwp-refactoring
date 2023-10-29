package kitchenpos.domain.table;

import kitchenpos.exception.ExceptionInformation;
import kitchenpos.exception.KitchenposException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    private static final int MIN_ORDER_TABLE_SIZE_IN_GROUP = 2;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    protected OrderTables() {

    }

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables create(final List<OrderTable> orderTables) {
        validateGroupSize(orderTables);
        validateOrderTableStatus(orderTables);
        return new OrderTables(orderTables);
    }

    private static void validateGroupSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_SIZE_IN_GROUP) {
            throw new KitchenposException(ExceptionInformation.TABLE_GROUP_UNDER_BOUNCE);
        }
    }

    private static void validateOrderTableStatus(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.unableGrouping()) {
                throw new KitchenposException(ExceptionInformation.TABLE_GROUP_NOT_EMPTY_OR_ALREADY_GROUPED);
            }
        }
    }

    public void updateOrderTablesGrouped(final Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(tableGroupId);
            orderTable.updateOrderStatus(false);
        }
    }

    public void updateOrderTablesUngrouped() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroupId();
            orderTable.updateOrderStatus(false);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
