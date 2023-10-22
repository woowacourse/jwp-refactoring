package kitchenpos.domain;

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

@Table(name = "table_group")
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(final Long id) {
        this.id = id;
    }

    public static TableGroup forSave() {
        return new TableGroup(null);
    }

    public void addOrderTable(final OrderTable orderTable) {
        orderTable.setTableGroup(this);
        orderTables.add(orderTable);
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.changeEmptyStatus();
        }
        orderTables.clear();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
