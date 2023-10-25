package kitchenpos.domain.table;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables from(final List<OrderTable> orderTables) {
        if (Objects.isNull(orderTables) || CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalStateException(String.format("OrderTables을 생성할 때는 최소 2개 이상의 OrderTable이 있어야 합니다. OrderTable = %s", orderTables));
        }

        final OrderTables result = new OrderTables();
        result.addAll(orderTables);
        return result;
    }

    public void addAll(final List<OrderTable> toAddOrderTables) {
        this.orderTables.addAll(toAddOrderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
