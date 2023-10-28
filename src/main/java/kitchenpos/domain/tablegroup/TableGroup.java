package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    public static final String ORDER_TABLE_SIZE_IS_BELOW_TWO_ERROR_MESSAGE = "테이블 그룹은 2개 이상의 테이블로 구성되어야 합니다.";
    public static final String TABLE_IS_IN_TABLE_GROUP_ERROR_MESSAGE = "이미 테이블 그룹에 속해있는 테이블입니다.";
    public static final String ORDERABLE_TABLE_IS_NOT_ALLOWED_ERROR_MESSAGE = "주문 불가능한 테이블만 그룹 지정이 가능합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime createdDate;
    @NotNull
    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final List<OrderTable> orderTables) {
        validateHasTableGroup(orderTables);
        orderTables.forEach(TableGroup::validateOrderTableIsUnOrderable);
        validateOrderTableSize(orderTables);
        setOrderTableOrderable(orderTables);
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    private static void validateOrderTableIsUnOrderable(final OrderTable orderTable) {
        if (orderTable.isOrderable()) {
            throw new IllegalArgumentException(ORDERABLE_TABLE_IS_NOT_ALLOWED_ERROR_MESSAGE);
        }
    }

    private static void validateHasTableGroup(final List<OrderTable> orderTables) {
        orderTables.forEach(TableGroup::checkOrderTableHasGroup);
    }

    private static void checkOrderTableHasGroup(final OrderTable orderTable) {
        if (orderTable.getTableGroupId().isPresent()) {
            throw new IllegalArgumentException(TABLE_IS_IN_TABLE_GROUP_ERROR_MESSAGE);
        }
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException(ORDER_TABLE_SIZE_IS_BELOW_TWO_ERROR_MESSAGE);
        }
    }

    private static void setOrderTableOrderable(final List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.setOrderable(true));
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> {
            orderTable.setTableGroupId(null);
            orderTable.setOrderable(false);
        });
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
}
