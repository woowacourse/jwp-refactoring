package kitchenpos.tablegroup;

import kitchenpos.ordertable.NoOngoingOrderValidator;
import kitchenpos.ordertable.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
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

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public void add(List<OrderTable> orderTables) {
        validateSizeOf(orderTables);
        this.orderTables = orderTables;
        orderTables.forEach(table -> table.assign(id));
    }

    private void validateSizeOf(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(MINIMUM_GROUP_SIZE + "개 이상의 테이블이 필요합니다");
        }
    }

    public void ungroupUsing(NoOngoingOrderValidator noOngoingOrderValidator) {
        for (OrderTable orderTable : orderTables) {
            noOngoingOrderValidator.validate(orderTable);
            orderTable.ungroup();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
