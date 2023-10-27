package kitchenpos.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.OrderTableException.EmptyTableException;
import kitchenpos.exception.TableGroupException.InvalidOrderTablesException;
import kitchenpos.exception.TableGroupException.UngroupingNotPossibleException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static TableGroup of(final List<OrderTable> orderTables) {
        final TableGroup tableGroup = new TableGroup(orderTables);

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_ORDER_TABLE_SIZE) {
            throw new InvalidOrderTablesException();
        }

        if (orderTables.stream()
            .anyMatch(OrderTable::isEmpty)) {
            throw new EmptyTableException();
        }

        return tableGroup;
    }

    public void ungroup(final OrderStatusChecker orderStatusChecker) {
        if (orderStatusChecker.checkEnableUngroupingTableGroup(id)) {
            throw new UngroupingNotPossibleException();
        }
        orderTables = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
