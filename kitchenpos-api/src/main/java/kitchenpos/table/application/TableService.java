package kitchenpos.table.application;

import kitchenpos.table.application.request.TableCreateRequest;
import kitchenpos.table.application.request.TableEmptyUpdateRequest;
import kitchenpos.table.application.request.TableGuestUpdateRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.OrderStatusValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableService {
    private OrderTableRepository orderTableRepository;
    private OrderStatusValidator orderStatusValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderStatusValidator orderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusValidator = orderStatusValidator;
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

        orderStatusValidator.checkIfOrderIsNotCompleted(orderTableId);

        savedOrderTable.changeEmpty(tableEmptyUpdateRequest.getEmpty());

        return OrderTableResponse.from(savedOrderTable);
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
