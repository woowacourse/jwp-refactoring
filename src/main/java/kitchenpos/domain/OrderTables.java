package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상이여야 합니다");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
