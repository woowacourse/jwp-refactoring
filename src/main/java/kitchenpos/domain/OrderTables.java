package kitchenpos.domain;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void grouping() {
        this.orderTables.forEach(OrderTable::changeToUse);
    }

    public void ugrouping() {
        this.orderTables.forEach(OrderTable::changeToEmpty);
    }

    public void validateTableCount() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정은 3개 이상의 정수로 입력해주세요.");
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
