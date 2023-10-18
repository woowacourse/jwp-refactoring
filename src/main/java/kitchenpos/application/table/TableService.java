package kitchenpos.application.table;

import kitchenpos.application.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.application.table.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.table.dto.OrderTableCreateRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.CannotUnGroupBecauseOfStatusException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {

    private static final List<String> UNGROUP_ORDER_STATUSES = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest req) {
        OrderTable orderTable = req.toDomain();
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest req) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        validateStatus(savedOrderTable);

        savedOrderTable.updateEmptyStatus(req.isEmpty());
        return savedOrderTable;
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private void validateStatus(final OrderTable savedOrderTable) {
        if (isStatusCookingOrMeal(savedOrderTable)) {
            throw new CannotUnGroupBecauseOfStatusException();
        }
    }

    private boolean isStatusCookingOrMeal(final OrderTable savedOrderTable) {
        return orderRepository.existsByOrderTableAndOrderStatusIsIn(
                savedOrderTable, UNGROUP_ORDER_STATUSES
        );
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestRequest req) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(req.getNumberOfGuests());

        return savedOrderTable;
    }
}
