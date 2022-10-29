package kitchenpos.domain.tablegroup;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validate(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(id);
            orderTable.setEmpty(false);
        }
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup of(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        return of(null, createdDate, orderTables);
    }

    public static TableGroup createForEntity(final Long id,
                                             final LocalDateTime createdDate) {
        return new TableGroup(id, createdDate, null);
    }

    private static void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블 없이 테이블 그룹을 생성할 수 없습니다.");
        }
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("주문 테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없습니다.");
            }
            if (Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("이미 테이블 그룹을 가진 주문 테이블은 테이블 그룹을 생성할 수 없습니다.");
            }
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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableStatus(orderTable);
            orderTable.changeTableGroup(null);
            orderTable.setEmpty(false);
        }
        this.orderTables = new LinkedList<>();
    }

    private void validateOrderTableStatus(final OrderTable orderTable) {
        if (orderTable.isCooking() || orderTable.isMeal()) {
            throw new IllegalArgumentException("조리중이거나 식사중이면 그룹을 해제할 수 없습니다.");
        }
    }
}
