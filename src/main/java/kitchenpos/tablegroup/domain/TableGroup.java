package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.ordertable.domain.OrderTable;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected TableGroup() {}

    private TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup create(final List<OrderTable> orderTables) {
        return new TableGroup(LocalDateTime.now(), orderTables);
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
