package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.ui.dto.ChangeEmptyRequest;
import kitchenpos.table.ui.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.ui.dto.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
        final OrderTable orderTable = orderTableRepository.save(request.toEntity());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        if (hasNotCompletedOrder(orderTableId)) {
            throw new IllegalArgumentException();
        }

        final OrderTable updatedOrderTable = orderTableRepository.getOrderTable(orderTableId)
                .changeEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(updatedOrderTable));
    }

    private boolean hasNotCompletedOrder(final Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final OrderTable updatedOrderTable = orderTableRepository.getOrderTable(orderTableId)
                .changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(updatedOrderTable));
    }
}
