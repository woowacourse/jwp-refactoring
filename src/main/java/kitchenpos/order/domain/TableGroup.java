package kitchenpos.order.domain;

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

import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderTable;

/**
 * 통합 계산을 위해 개별 OrderTable 을 그룹화하는 객체
 */
@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.orderTables = List.copyOf(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        validateOrderTables();
        groupOrderTables();
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(LocalDateTime.now(), orderTables);
    }

    private void validateOrderTables() {
        validateOrderTablesSize();
        validateOrderTablesCanGroup();
    }

    private void validateOrderTablesSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesCanGroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateCanBeGrouped();
        }
    }

    private void groupOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(id);
        }
    }

    public void ungroupOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return List.copyOf(orderTables);
    }
}
