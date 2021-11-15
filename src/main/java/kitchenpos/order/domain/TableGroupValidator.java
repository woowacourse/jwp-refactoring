package kitchenpos.order.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class TableGroupValidator {

    private final OrderRepository orderRepository;

    public TableGroupValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateGroupCreation(TableGroup tableGroup, List<Long> orderTableIds) {
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateGroupDeletion(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 중, 식사 중에 있는 주문 테이블의 그룹을 해제할 수 없습니다.");
        }
    }
}
