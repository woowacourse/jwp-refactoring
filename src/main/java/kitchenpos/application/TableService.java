package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.ChangeNumOfTableGuestsRequest;
import kitchenpos.application.dto.request.ChangeOrderTableEmptyRequest;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return new OrderTableResponse(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, ChangeOrderTableEmptyRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validateTableCanChangeEmpty(orderTableId);

        orderTable.changeEmptyStatus(request.isEmpty());

        return new OrderTableResponse(orderTable);
    }

    private void validateTableCanChangeEmpty(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("비울 수 없는 테이블이 존재합니다.");
        }
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, ChangeNumOfTableGuestsRequest request) {
        OrderTable orderTable = findOrderTable(orderTableId);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }
}
