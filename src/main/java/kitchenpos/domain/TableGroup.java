package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    private static final int MIN_ORDER_TABLES_SIZE = 2;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(final Long id,
                      final List<OrderTable> orderTables) {
        validateToConstruct(orderTables);
        this.id = id;
        addOrderTables(orderTables);
    }

    private void validateToConstruct(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException(
                String.format(
                    "빈 상태가 아니거나 이미 그룹에 존재하는 테이블로는 그룹을 만들 수 없습니다.(id: %d)",
                    orderTable.getId()
                )
            );
        }
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLES_SIZE) {
            throw new IllegalArgumentException(
                String.format(
                    "주문 테이블은 최소 %d개 이상이어야 합니다.(현재 개수: %d개)",
                    MIN_ORDER_TABLES_SIZE,
                    orderTables.size()
                )
            );
        }
    }

    private void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.changeTableGroup(this);
        }
    }

    public void removeAllOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false);
        }
        orderTables = null;
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
