package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.CannotUnGroupBecauseOfStatusException;
import kitchenpos.ordertable.exception.OrderTableNotFoundException;
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
        return orderRepository.existsByOrderTableIdAndOrderStatusIsIn(
                savedOrderTable.getId(), UNGROUP_ORDER_STATUSES
        );
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestRequest req) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.updateNumberOfGuests(req.getNumberOfGuests());

        return savedOrderTable;
    }

    @Transactional(readOnly = true)
    public boolean isExistById(final Long id) {
        return orderTableRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isExistsByIdAndEmptyIsFalse(final Long id) {
        return orderTableRepository.existsByIdAndEmptyIsFalse(id);
    }
}
