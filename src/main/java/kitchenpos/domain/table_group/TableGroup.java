package kitchenpos.domain.table_group;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.AggregateReference;
import org.springframework.data.domain.DomainEvents;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final LocalDateTime createdDate;
    private transient TableGroupEvent tableGroupEvent;

    public TableGroup() {
        this.id = null;
        this.createdDate = null;
    }

    public TableGroup(
            final List<AggregateReference<OrderTable>> orderTables,
            final LocalDateTime createdDate,
            final TableGroupValidator tableGroupValidator
    ) {
        id = null;
        this.createdDate = createdDate;
        tableGroupValidator.validate(orderTables);
        group(orderTables);
    }

    private void group(
            final List<AggregateReference<OrderTable>> orderTables
    ) {
        this.tableGroupEvent = new CreateTableGroupEvent(orderTables, this);
    }

    public void ungroup() {
        this.tableGroupEvent =  new DeleteTableGroupEvent(this);
    }

    @DomainEvents
    private TableGroupEvent publishEvent() {
        return tableGroupEvent;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
