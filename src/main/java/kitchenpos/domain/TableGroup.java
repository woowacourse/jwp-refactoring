package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {

    private static final int MIN_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, LocalDateTime.now(), orderTables);
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException(String.format("테이블 그룹은 최소 %s개 이상의 테이블이 필요합니다.", MIN_ORDER_TABLE_SIZE));
        }

        if (orderTables.stream().anyMatch(OrderTable::isNotEmpty)) {
            throw new IllegalArgumentException("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }

        if (orderTables.stream().anyMatch(it -> Objects.nonNull(it.getTableGroupId()))) {
            throw new IllegalArgumentException("비어있는 테이블만 주문그룹이 될 수 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
