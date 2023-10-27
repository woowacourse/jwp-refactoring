package kitchenpos.tablegroup.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.BaseEntity;
import kitchenpos.table.domain.OrderTable;

@Entity
public class TableGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    private TableGroup(
            final Long id,
            final List<OrderTable> orderTables
    ) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.updateTableGroup(null));
        orderTables = null;
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
