package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this(id, createdDate, new OrderTables(orderTables));
    }

    public TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesUsing(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateOrderTablesSize(final OrderTables orderTables) {
        if (orderTables.isEmpty() || orderTables.isSmallerThen(2)) {
            throw new IllegalArgumentException("테이블 그룹을 생성할 수 없는 주문 테이블 사이즈 입니다.");
        }
    }

    private void validateOrderTablesUsing(final OrderTables orderTables) {
        if (orderTables.isUsing()) {
            throw new IllegalArgumentException("이미 사용중인 주문 테이블이 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }
}
