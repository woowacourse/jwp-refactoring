package kitchenpos.domain.tablegroup;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    private static final int MINIMUM_GROUP_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate = LocalDateTime.now();
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validateSizeOf(orderTables);
        this.orderTables = orderTables;
        // TODO: 테이블에 그룹을 직접 연관관계 설정 해야할까?
        orderTables.forEach(table -> table.assign(this));
    }

    private void validateSizeOf(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(MINIMUM_GROUP_SIZE + "개 이상의 테이블이 필요합니다");
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
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
