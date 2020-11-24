package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableGroupVerifier {
    private static final int MIN_TABLE_COUNT = 2;

    private final OrderRepository orderRepository;

    public TableGroupVerifier(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void verifyOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_TABLE_COUNT) {
            throw new IllegalArgumentException(
                "단체 지정할 주문 테이블은 " + MIN_TABLE_COUNT + "개 이상이어야 합니다.");
        }
    }

    public void verifyNotCompletedOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 존재하지 않아야 합니다.");
        }
    }
}
