package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.NotFoundOrderException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderNotCompletionException;
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
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        final OrderTable orderTable = orderTableCreateRequest.toOrderTable();
        final OrderTable saved = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean changeOrderEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderException::new);

        validateOrderCompletion(orderTableId);
        savedOrderTable.updateEmpty(changeOrderEmpty);
        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateOrderCompletion(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new OrderNotCompletionException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int changeNumberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);

        savedOrderTable.updateNumberOfGuests(changeNumberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }
}
