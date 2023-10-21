package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED;
import static kitchenpos.domain.exception.TableGroupExceptionType.ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.exception.TableGroupException;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {

    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.createdDate = createdDate;
        groupOrderTables(orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableCanGroup(orderTable);
        }
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException(ORDER_TABLE_SIZE_IS_LOWER_THAN_ZERO_OR_EMPTY);
        }
    }

    private static void validateOrderTableCanGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new TableGroupException(ORDER_TABLE_IS_NOT_EMPTY_OR_ALREADY_GROUPED);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void groupOrderTables(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.changeEmpty(false);
            savedOrderTable.changeTableGroup(this);
        }
        orderTables.addAll(savedOrderTables);
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false);
        }
        orderTables.clear();
    }
}
