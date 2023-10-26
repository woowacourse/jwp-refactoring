package kitchenpos.tablegroup;

import kitchenpos.order.Order;
import kitchenpos.order.OrderRepository;
import kitchenpos.table.OrderTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnGroupValidator {

    private final OrderRepository orderRepository;

    public UnGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(TableGroup tableGroup) {
        List<Long> orderTableIds = tableGroup.getOrderTableIds();
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        if (orders.stream()
                .anyMatch(Order::isCookingOrMeal)) {
            throw new IllegalArgumentException("조리중 또는 식사중인 테이블은 그룹을 해제할 수 없습니다.");
        }
        if (tableGroup.getOrderTables().stream()
                .anyMatch(OrderTable::hasNoTableGroup)) {
            throw new IllegalArgumentException("그룹으로 지정되지 않은 테이블은 그룹을 해제할 수 없습니다.");
        }
    }
}
