package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.repository.OrderRepository;

@Component
public class OrderTableValidator {
    private static final List<String> ORDER_STATUS_FOR_CANT_CHANGE_EMPTY = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateGroup(OrderTable orderTable) {
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("이미 다른 그룹에 존재하는 테이블입니다.");
        }

        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블입니다.");
        }
    }

    public void validateChangeEmpty(OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(),
            ORDER_STATUS_FOR_CANT_CHANGE_EMPTY)) {
            throw new IllegalArgumentException("주문이 시작되어 상태를 변경할 수 없습니다.");
        }

        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("테이블 그룹에 묶여있어 상태를 변경할 수 없습니다.");
        }
    }

    public void validateChangeNumberOfGuests(int numberOfGuests, OrderTable orderTable) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0 이상이어야 합니다.");
        }

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블입니다.");
        }
    }

}
