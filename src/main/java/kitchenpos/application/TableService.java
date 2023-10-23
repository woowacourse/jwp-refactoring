package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderTableCreateRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.OrderStatus.*;

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
        return orderTableRepository.save(orderTable).getId();
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public void changeEmpty(final Long orderTableId, final boolean isEmpty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateTableGroupIsNonNull();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.updateEmpty(isEmpty);
    }

    public void changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.validateNumberOfGuests();
        orderTable.validateIsEmpty();
        orderTable.updateNumberOfGuests(numberOfGuests);
    }
}
