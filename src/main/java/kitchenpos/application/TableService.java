package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableChangeStatusRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.from(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeStatusRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateCompletionStatus(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateCompletionStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
