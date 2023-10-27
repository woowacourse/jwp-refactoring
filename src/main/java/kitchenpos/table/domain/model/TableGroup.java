package kitchenpos.table.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.table.domain.service.TableGroupValidator;
import kitchenpos.table.domain.service.TableUngroupValidator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "order_table_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroup create(List<OrderTable> orderTables, TableGroupValidator tableGroupValidator) {
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroupValidator.validate(tableGroup);
        return tableGroup;
    }

    public void ungroup(TableUngroupValidator tableUngroupValidator) {
        tableUngroupValidator.validate(this);
        orderTables.forEach(OrderTable::ungroup);
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
