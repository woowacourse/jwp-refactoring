package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.CreateOrderTableRequest;
import kitchenpos.table.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.table.application.dto.request.UpdateOrderTableGuests;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(CreateOrderTableRequest createOrderTableRequest) {
        OrderTable orderTable = new OrderTable(createOrderTableRequest.getNumberOfGuests(), createOrderTableRequest.isEmpty());
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

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, UpdateOrderTableEmptyRequest updateOrderTableEmptyRequest) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateIsOrderNotCompleted(orderTableId);
        orderTable.changeEmpty(updateOrderTableEmptyRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateIsOrderNotCompleted(Long orderTableId) {
        tableValidator.validateIsTableCompleteMeal(orderTableId);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, UpdateOrderTableGuests updateOrderTableGuests) {
        int numberOfGuests = updateOrderTableGuests.getNumberOfGuests();
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }
}
