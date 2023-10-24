package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.vo.OrderTables;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(final LocalDateTime createdDate) {
        this(null, createdDate);
    }

    public TableGroup(final Long id, final LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static TableGroup createWithNowCreatedDate() {
        return new TableGroup(LocalDateTime.now());
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 없거나 2개 미만일 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.tableGroup())) {
                throw new IllegalArgumentException("비어있지 않거나 테이블 그룹을 가진 주문 테이블이 존재합니다.");
            }
        }

        orderTables.forEach(orderTable -> {
            orderTable.changeEmpty(false);
            orderTable.setTableGroup(this);
        });
        this.orderTables.addAll(orderTables);
    }

    public Long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    public OrderTables orderTables() {
        return orderTables;
    }
}
