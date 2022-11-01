package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.badrequest.AlreadyGroupedException;
import kitchenpos.exception.badrequest.InvalidOrderTableSizeException;
import kitchenpos.exception.badrequest.OrderTableNotEmptyException;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        group(orderTables);
    }

    private void group(final List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(orderTable -> orderTable.designateTableGroup(this));
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateEmptyTable(orderTable);
            validateNotGrouped(orderTable);
        }
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTableSizeException();
        }
    }

    private void validateEmptyTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new OrderTableNotEmptyException();
        }
    }

    private void validateNotGrouped(final OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new AlreadyGroupedException();
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
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
}
