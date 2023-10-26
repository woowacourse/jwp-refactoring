package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Long create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = OrderTable.of(request.getNumberOfGuests(), request.getEmpty());
        final OrderTable saveOrderTable = orderTableRepository.save(orderTable);
        return saveOrderTable.getId();
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public void changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.getById(orderTableId);
        savedOrderTable.validateTableGroupIsNonNull();
        checkOrderExistsWithStatus(orderTableId);
        savedOrderTable.updateEmpty(isEmpty);
    }

    private void checkOrderExistsWithStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);
        orderTable.validateNumberOfGuests();
        orderTable.validateIsEmpty();
        orderTable.updateNumberOfGuests(numberOfGuests);
    }
}
