package kitchenpos.domain;

import kitchenpos.domain.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class OrderTableValidatorImpl implements OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateChangeEmpty(final Long orderTableId, final TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중 또는 식사중인 주문 테이블은 빈 테이블로 변경할 수 없습니다.");
        }

        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("그룹 지정된 테이블은 빈 테이블로 변경할 수 없습니다.");
        }
    }

    @Override
    public void validateUngroup(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않은 상태의 테이블이 존재합니다.");
        }
    }
}
