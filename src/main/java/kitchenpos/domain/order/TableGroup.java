package kitchenpos.domain.order;

import kitchenpos.exception.TableGroupException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    private final static Integer MINIMUM_GROUP_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        for (OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
            this.orderTables.add(orderTable);
            orderTable.changeTableGroup(this);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (isNull(orderTables) || isLessThanMinimumGroupSize(orderTables)) {
            throw new TableGroupException("주문 테이블이 1개 이하라 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.existsTableGroup()) {
            throw new TableGroupException("주문 테이블이 이미 테이블 그룹에 속해 있어 테이블 그룹을 생성할 수 없습니다.");
        }

        if (!orderTable.getEmpty()) {
            throw new TableGroupException("주문 테이블이 주문이 가능한 상태여서 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private boolean isNull(List<OrderTable> orderTables) {
        return Objects.isNull(orderTables);
    }

    private boolean isLessThanMinimumGroupSize(List<OrderTable> orderTables) {
        return orderTables.size() < MINIMUM_GROUP_SIZE;
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
