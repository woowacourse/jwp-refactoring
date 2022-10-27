package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

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

    public TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.orderTables = List.copyOf(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        validateOrderTables(this.orderTables);
        groupOrderTables(orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this(id, createdDate, null);
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(LocalDateTime.now(), orderTables);
    }

    private void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesCanGroup(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void groupOrderTables(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(id);
            orderTable.setEmpty(false);
        }
    }

    public Long getId() {
        return id;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return List.copyOf(orderTables);
    }

    public void unGroup() {
    }
}
