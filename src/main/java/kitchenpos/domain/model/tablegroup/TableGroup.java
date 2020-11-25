package kitchenpos.domain.model.tablegroup;

import static java.util.stream.Collectors.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.domain.model.ordertable.OrderTable;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "table_group_id", nullable = false)
    private List<OrderTable> orderTables;

    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(Long id, List<OrderTable> orderTables,
            LocalDateTime createdDate) {
        this.id = id;
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public TableGroup create() {
        createdDate = LocalDateTime.now();
        return this;
    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
