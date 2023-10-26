package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderTableEmptyChangeRequest;
import kitchenpos.dto.OrderTableNumberOfGuestsChangeRequest;
import kitchenpos.dto.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        return orderTableRepository.save(
                new OrderTable(null, request.getTableGroupId(), request.getNumberOfGuests(), request.getEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        validateOrderTableForChangeEmpty(savedOrderTable);
        savedOrderTable.changeEmpty(request.getEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private void validateOrderTableForChangeEmpty(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리중 or 식사중인 주문이 포함되어 있습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableNumberOfGuestsChangeRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
