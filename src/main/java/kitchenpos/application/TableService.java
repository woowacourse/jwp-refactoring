package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.dto.OrderTableChangeEmptyRequest;
import kitchenpos.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.CannotChangeGroupedTableEmptyException;
import kitchenpos.exception.InvalidRequestParameterException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.UnCompletedOrderExistsException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository,
            OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(
                new OrderTable(request.getNumberOfGuests(), request.getEmpty()));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new CannotChangeGroupedTableEmptyException();
        }

        List<Orders> orders = orderRepository.findAllByOrderTable(orderTable);
        boolean isStatusNotChangeable = orders.stream()
                .anyMatch(Orders::isOrderUnCompleted);
        if (isStatusNotChangeable) {
            throw new UnCompletedOrderExistsException();
        }

        orderTable.setEmpty(request.getEmpty());

        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
            OrderTableChangeNumberOfGuestsRequest request) {
        if (request.getNumberOfGuests() < 0) {
            throw new InvalidRequestParameterException();
        }

        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderTableNotFoundException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.from(orderTable);
    }
}
