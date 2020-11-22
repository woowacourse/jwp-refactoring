package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class TableOrderEmptyValidator {
    private final OrderRepository orderRepository;

    public TableOrderEmptyValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(Long tableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            tableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public void validate(List<Long> tableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            tableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
