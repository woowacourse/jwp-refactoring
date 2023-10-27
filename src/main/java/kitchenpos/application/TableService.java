package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.TableCreateRequest;
import kitchenpos.ui.request.TableEmptyUpdateRequest;
import kitchenpos.ui.request.TableGuestUpdateRequest;
import kitchenpos.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {

    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final TableCreateRequest tableCreateRequest) {
        OrderTable orderTable = new OrderTable(null, tableCreateRequest.getNumberOfGuests(), tableCreateRequest.getEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, TableEmptyUpdateRequest tableEmptyUpdateRequest) {
        OrderTable savedOrderTable = getOrderTable(orderTableId);

        checkIfOrderIsNotCompleted(orderTableId);

        savedOrderTable.changeEmpty(tableEmptyUpdateRequest.getEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    private void checkIfOrderIsNotCompleted(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, TableGuestUpdateRequest tableGuestUpdateRequest) {
        OrderTable savedOrderTable = getOrderTable(orderTableId);

        savedOrderTable.changeNumberOfGuest(tableGuestUpdateRequest.getNumberOfGuests());

        return OrderTableResponse.from(savedOrderTable);
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
