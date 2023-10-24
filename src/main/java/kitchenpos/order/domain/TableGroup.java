package kitchenpos.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private GroupedOrderTables groupedOrderTables;

    @CreatedDate
    @NotNull
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(GroupedOrderTables groupedOrderTables) {
        this.groupedOrderTables = groupedOrderTables;
    }

    public static TableGroup createWithGrouping(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(GroupedOrderTables.from(orderTables));
        orderTables.forEach(orderTable -> orderTable.group(tableGroup));

        return tableGroup;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return groupedOrderTables.getOrderTables();
    }

}
