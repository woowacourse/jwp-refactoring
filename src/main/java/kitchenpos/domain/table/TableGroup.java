package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException.GroupAlreadyExistsException;
import kitchenpos.domain.exception.TableGroupException.InvalidOrderTablesException;
import kitchenpos.domain.exception.TableGroupException.UngroupingNotPossibleException;
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

        for (final OrderTable orderTable : orderTables) {
            if (orderTable.isEmpty()) {
                throw new GroupAlreadyExistsException();
            }
        }

        return tableGroup;
    }

    public void ungroup(final OrderStatusChecker orderStatusChecker) {
        if (orderStatusChecker.checkUngroupableTableGroup(id)) {
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
