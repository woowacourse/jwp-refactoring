package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.application.TableValidator;
import kitchenpos.application.TableMapper;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "datetime(6)")
    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(
            LocalDateTime createdDate,
            List<Long> tableIds,
            TableMapper tableMapper,
            TableValidator tableValidator
    ) {
        this.createdDate = createdDate;
        List<OrderTable> orderTables = tableMapper.toOrderTables(tableIds);
        tableValidator.validateTablesForGroup(orderTables);
        this.orderTables.addAll(orderTables);
        group(this.orderTables);
    }

    private void group(List<OrderTable> orderTables) {
        orderTables.forEach(OrderTable::group);
    }

    public void ungroup(TableValidator tableValidator) {
        tableValidator.validateUpGroup(orderTables);
        orderTables.forEach(OrderTable::unGroup);
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
