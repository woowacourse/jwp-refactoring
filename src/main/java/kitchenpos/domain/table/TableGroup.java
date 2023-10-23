package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException.GroupAlreadyExistsException;
import kitchenpos.domain.exception.TableGroupException.InvalidOrderTablesException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
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
            if (orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new GroupAlreadyExistsException();
            }
        }

        orderTables.forEach(orderTable -> orderTable.changeTableGroup(tableGroup));
        return tableGroup;
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

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.removeAll(orderTables);
    }
}
