package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableCreateRequest;
import kitchenpos.dto.table.OrderTableIsEmptyUpdateRequest;
import kitchenpos.dto.table.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toEmptyOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeIsEmpty(Long orderTableId, OrderTableIsEmptyUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        boolean hasCookingOrMealOrder = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                List.of(COOKING, MEAL)
        );

        orderTable.changeIsEmpty(hasCookingOrMealOrder, request.isEmpty());
        OrderTable changedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(changedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableNumberOfGuestsUpdateRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.from(changedOrderTable);
    }
}
