package kitchenpos.tablegroup;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.OrderTable;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @Embedded
    private GroupedOrderTables groupedOrderTables;

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime localDateTime) {
        this.id = id;
        this.createdDate = localDateTime;
    }

    public static TableGroup createEmpty(LocalDateTime createdDate) {
        return new TableGroup(null, createdDate);
    }

    public void group(List<OrderTable> orderTables) {
        this.groupedOrderTables = new GroupedOrderTables(orderTables);
        groupedOrderTables.group(this);
    }

    public void ungroup() {
        groupedOrderTables.ungroup();
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return groupedOrderTables.getValues();
    }
}
