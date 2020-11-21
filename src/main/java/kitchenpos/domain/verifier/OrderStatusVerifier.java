package kitchenpos.domain.verifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.OrderNotCompleteException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Component
public class OrderStatusVerifier implements OrderVerifier {
    private static final List<OrderStatus> NOT_COMPLETE_STATUS = Arrays.asList(OrderStatus.COOKING,
        OrderStatus.MEAL);
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderStatusVerifier(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable verifyOrderStatusByTableId(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            NOT_COMPLETE_STATUS)) {
            throw new OrderNotCompleteException(orderTableId);
        }

        return orderTable;
    }

    @Override
    public List<OrderTable> verifyOrderStatusByTableGroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> ids = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(ids, NOT_COMPLETE_STATUS)) {
            throw new OrderNotCompleteException();
        }

        return orderTables;
    }
}
