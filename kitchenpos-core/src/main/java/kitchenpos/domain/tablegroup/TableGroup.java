package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    private OrderTables orderTables;

    protected TableGroup() {
    }

    private TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup create(final List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), OrderTables.create(orderTables));
    }


    public void updateOrderTablesGrouped() {
        orderTables.updateOrderTablesGrouped(id);

    }

    public void updateOrderTablesUngrouped() {
        orderTables.updateOrderTablesUngrouped();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }

    public Long getId() {
        return id;
    }


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }


    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}
