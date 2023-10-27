package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateChangeEmpty(final OrderTable orderTable) {
        final Long orderTableId = orderTable.getId();

        final Long tableGroupId = orderTable.getTableGroupId();
        if (tableGroupId != null && orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 테이블 그룹이 존재하는 경우에 혼자 테이블을 비울 수 없습니다.");
        }

        if (isCanUngroupOrChangeEmpty(orderTableId)) {
            throw new IllegalArgumentException("[ERROR] 조리중이거나, 식사중인 테이블을 비울 수 없습니다.");
        }
    }

    private boolean isCanUngroupOrChangeEmpty(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL));
    }

    public void validateChangeNumberOfGuests(final OrderTable orderTable) {
        if (orderTable.getNumberOfGuests() < 0) {
            throw new IllegalArgumentException("[ERROR] 손님의 숫자는 항상 0보다 커야 합니다.");
        }
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }
}
