package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.TableIdRequest;
import org.springframework.util.CollectionUtils;

public class TableGroup {

    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this(id, createdDate, new ArrayList<>());
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public static TableGroup create(List<OrderTable> orderTables, int actualSize) {
        validateOrderTableSize(orderTables);
        validateEachTableStatus(orderTables);
        validateOrderTableExistence(orderTables.size(), actualSize);

        return new TableGroup(null, orderTables);
    }

    private static void validateEachTableStatus(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateEmpty();
            orderTable.validateNotInTableGroup();
        }
    }

    private static void validateOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public static void validateOrderTableExistence(int orderTableSize, int actualSize) {
        if (orderTableSize != actualSize) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
