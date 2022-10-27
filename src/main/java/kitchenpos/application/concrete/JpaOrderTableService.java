package kitchenpos.application.concrete;

import java.util.List;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.request.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaOrderTableService implements TableService {
    private static final List<String> ACTIVE_ORDER_STATUS = List.of(
            OrderStatus.COOKING.name(),
            OrderStatus.MEAL.name()
    );

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public JpaOrderTableService(final OrderTableRepository orderTableRepository,
                                final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public OrderTable create(final OrderTableCreateRequest request) {
        final var newOrderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(newOrderTable);
    }

    @Override
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    @Override
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateDoesNotContainActiveOrder(orderTableId);

        return orderTable.changeEmpty(request.isEmpty());
    }

    private void validateDoesNotContainActiveOrder(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, ACTIVE_ORDER_STATUS)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    @Override
    public OrderTable changeNumberOfGuests(final Long orderTableId,
                                           final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        return orderTable.changeNumberOfGuests(request.getNumberOfGuests());
    }
}
