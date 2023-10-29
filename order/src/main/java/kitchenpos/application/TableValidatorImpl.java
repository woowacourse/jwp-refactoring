package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.OrderStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableValidatorImpl implements TableValidator {

    private final OrderRepository orderRepository;

    public TableValidatorImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmpty(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.stream()
                .filter(Order::isProgress)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("진행중인 주문이 있는 테이블은 수정할 수 없습니다.");
                });
    }

    public void validateTablesForGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹화를 위해서는 테이블이 2개 이상 필요합니다.");
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화할 수 없습니다.");
            }
            if (orderTable.isGrouped()) {
                throw new IllegalArgumentException("이미 그룹화된 테이블은 그룹화할 수 없습니다.");
            }
        }
    }

    public void validateUpGroup(List<OrderTable> orderTables) {
        List<Long> orderTableIds = extractOrderTableIds(orderTables);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블이 존재합니다.");
        }
    }

    private List<Long> extractOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
