package kitchenpos.domain.service;

import static java.util.Arrays.*;

import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;

@Service
public class TableGroupUngroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupUngroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void resetOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.resetTableGroup();
            orderTableRepository.save(orderTable);
        }
    }
}
