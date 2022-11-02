package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables, final LocalDateTime createdDate) {
        this(null, orderTables, createdDate);
    }

    private TableGroup(final Long id, final List<OrderTable> orderTables, final LocalDateTime createdDate) {
        validateTables(orderTables);
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
        for (OrderTable orderTable : this.orderTables) {
            orderTable.changeTableGroup(this);
        }
    }

    private void validateTables(final List<OrderTable> orderTables) {
        validateSize(orderTables);
        for (OrderTable orderTable : orderTables) {
            validateEmpty(orderTable);
            validateNotGrouped(orderTable);
        }
    }

    private void validateSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new DomainLogicException(CustomError.TABLE_GROUP_MIN_TABLES_ERROR);
        }
    }

    private void validateEmpty(final OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new DomainLogicException(CustomError.TABLE_GROUP_TABLE_NOT_EMPTY_ERROR);
        }
    }

    private void validateNotGrouped(final OrderTable orderTable) {
        if (orderTable.isGrouped()) {
            throw new DomainLogicException(CustomError.TABLE_ALREADY_GROUPED_ERROR);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.validateAllOrderCompleted();
        }
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

    public List<OrderTable> getTables() {
        return orderTables;
    }
}
