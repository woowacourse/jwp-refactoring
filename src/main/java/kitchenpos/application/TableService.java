package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.application.exceptions.NotExistedOrderTableException;
import kitchenpos.application.exceptions.OrderStatusNotCompletionException;
import kitchenpos.ui.dto.ordertable.OrderTableRequest;
import kitchenpos.ui.dto.ordertable.OrderTableResponse;
import kitchenpos.ui.dto.ordertable.OrderTableResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.from(savedOrderTable);
    }

    public OrderTableResponses list() {
        return OrderTableResponses.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotExistedOrderTableException::new);

        validateOrderStatus(orderTableId);

        savedOrderTable.changeEmptyState(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new OrderStatusNotCompletionException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotExistedOrderTableException::new);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
