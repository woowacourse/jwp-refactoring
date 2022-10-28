package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeEmptyRequest;
import kitchenpos.ui.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return toResponse(orderTableRepository.save(
                OrderTable.of(
                        request.getNumberOfGuests(),
                        request.isEmpty()
                )
        ));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        if (hasNotCompletedOrder(orderTableId)) {
            throw new IllegalArgumentException();
        }

        final OrderTable updatedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new)
                .changeEmpty(request.isEmpty());
        return toResponse(orderTableRepository.save(updatedOrderTable));
    }

    private boolean hasNotCompletedOrder(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        );
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final OrderTable updatedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new)
                .changeNumberOfGuests(request.getNumberOfGuests());
        return toResponse(orderTableRepository.save(updatedOrderTable));
    }

    private OrderTableResponse toResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}
