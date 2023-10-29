package kitchenpos.table;

import kitchenpos.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    private static final int MIN_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup() {
        this(null, LocalDateTime.now());
    }

    public void unGroup(TableValidator tableValidator, List<OrderTable> orderTables) {
        tableValidator.validateUngroupTableOrderCondition(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.changeEmpty();
        }
    }

    public void setOrderTables(final TableValidator tableValidator, final List<Long> requestOrderTableIds,
                               final List<OrderTable> orderTables) {
        tableValidator.validateGroupOrderTableExist(orderTables, requestOrderTableIds);

        validateOrderTableSize(orderTables);
        validateOrderTableCondition(orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(this.id);
        }
    }

    private void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("단체 지정 테이블의 개수는 2개 이상이어야 합니다.");
        }
    }

    private void validateOrderTableCondition(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("비어있지 않은 테이블이거나 이미 단체지정된 테이블입니다. 단체 지정할 수 없습니다.");
            }
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
