package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.tablegroup.domain.TableGroupOrderStatusValidator;
import org.springframework.stereotype.Component;

@Component
public class TableGroupOrderStatusValidatorImpl implements TableGroupOrderStatusValidator {

    private final OrderRepository orderRepository;

    public TableGroupOrderStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateOrderStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                                                                 Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 진행중인 주문이 존재하여, 테이블 그룹을 해제할 수 없습니다.");
        }
    }
}
