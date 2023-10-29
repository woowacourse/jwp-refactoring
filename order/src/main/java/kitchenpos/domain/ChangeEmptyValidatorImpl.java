package kitchenpos.domain;

import java.util.Arrays;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class ChangeEmptyValidatorImpl implements ChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public ChangeEmptyValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(final OrderTable orderTable) {
        if (nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("그룹이 지정된 테이블은 empty로 설정할 수 없습니다.");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.NOT_STARTED, OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 경우 테이블의 비어있을 수 없습니다.");
        }

    }

}
