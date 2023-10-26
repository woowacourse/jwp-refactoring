package kitchenpos.tablegroup;

import kitchenpos.table.OrderTable;
import kitchenpos.table.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "table_group")
@Entity
public class TableGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.id = null;
        this.orderTables = new OrderTables(orderTables);
    }

    public void unGroup() {
        orderTables.unGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getCollection();
    }

    public List<Long> getOrderTableIds() {
        return orderTables.getOrderTableIds();
    }
}
