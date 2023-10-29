package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.table.OrderTable;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validate(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
        }
    }

    private void validate(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableEmpty(orderTable);
            validateGrouped(orderTable);
        }
    }

    private void validateGrouped(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableEmpty(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
