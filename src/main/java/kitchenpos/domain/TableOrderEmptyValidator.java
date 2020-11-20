package kitchenpos.domain;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import kitchenpos.dao.OrderDao;

@Service
public class TableOrderEmptyValidator {
    private final OrderDao orderRepository;

    public TableOrderEmptyValidator(OrderDao orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(Long tableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            tableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
