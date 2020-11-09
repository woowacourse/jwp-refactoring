package kitchenpos.domain.service;

import static java.util.Arrays.*;
import static java.util.Objects.*;

import org.springframework.stereotype.Service;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;

@Service
public class OrderTableChangeEmptyService {
    private final OrderRepository orderRepository;

    public OrderTableChangeEmptyService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(Long id, Long tableGroupId) {
        if (nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
