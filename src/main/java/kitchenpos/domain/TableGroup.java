package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.OrderTableCountNotEnoughException;

@Table(name = "table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void groupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupNotExists();
        }
        this.orderTables = orderTables;
        validateOrderTableCount();
    }

    private void validateOrderTableCount() {
        if (orderTables.size() < 2) {
            throw new OrderTableCountNotEnoughException();
        }
    }

    public void unGroupOrderTables() {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
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
