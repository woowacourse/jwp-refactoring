package kitchenpos.domain.ordertable;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final int MINIMUM_TABLE_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        group(tableGroup, orderTables);

        return new OrderTables(orderTables);
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateEmptyOrderTable(orderTables);
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new InvalidOrderTableSizeException();
        }
    }

    private static void validateEmptyOrderTable(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new InvalidEmptyOrderTableException();
            }
        }
    }

    private static void group(final TableGroup tableGroup, final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(tableGroup);
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
