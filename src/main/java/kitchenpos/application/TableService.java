package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableEmptyUpdateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableNumberOfGuestsUpdateRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable newOrderTable = OrderTable.withoutTableGroup(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(newOrderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyUpdateRequest request) {
        final Optional<Order> maybeOrder = orderRepository.findByOrderTableId(orderTableId);
        if (maybeOrder.isPresent()) {
            return changeEmptyByOrder(maybeOrder.get(), request.isEmpty());
        }

        return changeEmptyByOrderTable(orderTableId, request.isEmpty());
    }

    private OrderTableResponse changeEmptyByOrder(final Order findOrder, final boolean isEmpty) {
        findOrder.changeOrderTableEmpty(isEmpty);

        return OrderTableResponse.from(findOrder.getOrderTable());
    }

    private OrderTableResponse changeEmptyByOrderTable(final Long orderTableId, final boolean isEmpty) {
        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);
        findOrderTable.changeOrderTableEmpty(isEmpty);

        return OrderTableResponse.from(findOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableNumberOfGuestsUpdateRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(orderTableId);
        findOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(findOrderTable);
    }
}
