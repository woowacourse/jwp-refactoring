package kitchenpos.domain.table;

import kitchenpos.exception.KitchenposException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.exception.ExceptionInformation.TABLE_GROUP_NOT_EMPTY_OR_ALREADY_GROUPED;
import static kitchenpos.exception.ExceptionInformation.TABLE_GROUP_UNDER_BOUNCE;

@Entity
public class TableGroup {
    private static final int MIN_TABLE_GROUP_BOUNCE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup create(final List<OrderTable> orderTables) {
        validateGroupSize(orderTables);
        validateOrderTableStatus(orderTables);
        return new TableGroup(LocalDateTime.now(), orderTables);
    }

    private static void validateGroupSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLE_GROUP_BOUNCE) {
            throw new KitchenposException(TABLE_GROUP_UNDER_BOUNCE);
        }
    }

    private static void validateOrderTableStatus(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (orderTable.unableGrouping()) {
                throw new KitchenposException(TABLE_GROUP_NOT_EMPTY_OR_ALREADY_GROUPED);
            }
        }
    }

    public void updateOrderTablesGrouped() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(this);
            orderTable.updateOrderStatus(false);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void updateOrderTablesUngrouped() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroupId();
            orderTable.updateOrderStatus(false);
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
}
