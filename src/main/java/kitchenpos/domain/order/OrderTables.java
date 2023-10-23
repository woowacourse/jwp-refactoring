package kitchenpos.domain.order;

import kitchenpos.exception.TableGroupException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    private final static Integer MINIMUM_GROUP_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public void add(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables.addAll(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (isNull(orderTables) || isLessThanMinimumGroupSize(orderTables)) {
            throw new TableGroupException("주문 테이블이 1개 이하라 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    private boolean isNull(List<OrderTable> orderTables) {
        return Objects.isNull(orderTables);
    }

    private boolean isLessThanMinimumGroupSize(List<OrderTable> orderTables) {
        return orderTables.size() < MINIMUM_GROUP_SIZE;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
