package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final int MIN_GROUP_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {}

    public TableGroup(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
        this.orderTables = new ArrayList<>();
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        validateTablesAbleToGroup(orderTables);
        this.orderTables = orderTables;
    }

    private void validateTablesAbleToGroup(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_GROUP_SIZE) {
            throw new IllegalArgumentException("그룹으로 묶을 테이블은 2개 이상이어야 합니다.");
        }
        for (final OrderTable orderTable : orderTables) {
            validateTableAbleToGroup(orderTable);
        }
    }

    private void validateTableAbleToGroup(final OrderTable orderTable) {
        if (orderTable.isUnableToBeGrouped()) {
            throw new IllegalArgumentException("그룹으로 묶을 수 없는 테이블입니다.");
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
