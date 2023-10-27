package kitchenpos.tablegroup.domain;

import kitchenpos.exception.InvalidOrderTableToTableGroup;
import kitchenpos.exception.InvalidOrderTablesSize;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {}

    private TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);

        final TableGroup tableGroup = new TableGroup(createdDate, orderTables);
        addOrderTables(orderTables, tableGroup);

        return tableGroup;
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTablesSize("주문 테이블은 2개 이상 있어야 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            validateOrderTableHasTableGroup(orderTable);
        }
    }

    private static void validateOrderTableHasTableGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new InvalidOrderTableToTableGroup("주문 테이블이 테이블 그룹을 만들 수 없는 상태입니다.");
        }
    }

    private static void addOrderTables(final List<OrderTable> orderTables, final TableGroup tableGroup) {
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.updateTableGroup(tableGroup);
            savedOrderTable.updateEmpty(false);
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
               "id=" + id +
               ", createdDate=" + createdDate +
               '}';
    }
}
