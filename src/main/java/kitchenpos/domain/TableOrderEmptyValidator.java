package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

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

    public void validate(List<Long> tableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            tableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
