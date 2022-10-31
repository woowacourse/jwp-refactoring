package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    private TableGroup() {
    }

    private TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        validate(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.changeTableGroup(id);
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
        validateOrderTables(orderTables);
    }

    private static void validateOrderTables(final List<OrderTable> orderTables) {
        validateEmptyOrderTables(orderTables);
        validateOrderTableSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private static void validateEmptyOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블 없이 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private static void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }
    }

    private static void validateOrderTable(final OrderTable orderTable) {
        validateEmptyOrderTable(orderTable);
        validateExistTableGroup(orderTable);
    }

    private static void validateEmptyOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private static void validateExistTableGroup(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 테이블 그룹을 가진 주문 테이블은 테이블 그룹을 생성할 수 없습니다.");
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

    public void changeOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void unGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false);
        }
        this.orderTables = new LinkedList<>();
    }
}
