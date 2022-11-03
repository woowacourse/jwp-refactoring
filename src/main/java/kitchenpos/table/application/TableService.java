package kitchenpos.table.application;

import static kitchenpos.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.ui.request.OrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;

    public TableService(final JpaOrderRepository orderRepository, final JpaOrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(request.toOrder());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        final List<OrderTableResponse> tableResponses = orderTables.stream().map(OrderTableResponse::from)
                .collect(Collectors.toList());
        return tableResponses;
    }

    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.validTableGroupCondition();
        validExistOrderTables(orderTableId);
        savedOrderTable.clearTable();

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validExistOrderTables(final Long orderTableId) {
        final Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        order.ifPresent(Order::validExistOrderStatus);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));
    }
}
