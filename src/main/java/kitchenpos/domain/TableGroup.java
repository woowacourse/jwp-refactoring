package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    protected TableGroup(final LocalDateTime createdDate, final OrderTables orderTables) {
        this(null, createdDate, orderTables);
    }

    protected TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup withOrderTables(final List<OrderTable> requestOrderTables) {
        if (requestOrderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정할 주문 테이블의 수가 2 미만일 수 없습니다.");
        }
        if (requestOrderTables.stream().anyMatch(OrderTable::isGrouped)) {
            throw new IllegalArgumentException("이미 단체 지정된 주문 테이블을 추가할 수 없습니다.");
        }
        if (requestOrderTables.stream().noneMatch(OrderTable::isEmpty)) {
            throw new IllegalArgumentException("비어있지 않은 주문 테이블을 단체 지정할 수 없습니다.");
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), OrderTables.empty());
        requestOrderTables.forEach(requestOrderTable -> requestOrderTable.changeOrderTableEmpty(false));
        tableGroup.addOrderTables(requestOrderTables);

        return tableGroup;
    }

    private void addOrderTables(final List<OrderTable> requestOrderTables) {
        final OrderTables newOrderTables = new OrderTables(requestOrderTables);
        orderTables.addOrderTables(newOrderTables);
        newOrderTables.assignTableGroup(this);
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
