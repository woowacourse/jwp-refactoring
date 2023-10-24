package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public void appendOrderTables(final List<OrderTable> orderTables) {
        validateSizeOrderTables(orderTables);
        validateEmptyOrderTables(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.group(this);
            this.orderTables.add(orderTable);
        }
    }

    private void validateSizeOrderTables(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }
    }

    private void validateEmptyOrderTables(final List<OrderTable> orderTables) {
        orderTables.forEach(this::validateEmptyOrderTable);
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }
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
