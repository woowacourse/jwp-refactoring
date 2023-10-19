package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    private static final int MINIMUM_ORDER_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;

    @JoinColumn(name = "tableGroupId")
    @OneToMany
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        if (isEmpty(orderTables) || orderTables.size() < MINIMUM_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException("그룹 지정은 주문 테이블이 최소 2개여야 합니다.");
        }
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    public void group() {
        orderTables.forEach(orderTable -> orderTable.groupBy(id));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
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

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
