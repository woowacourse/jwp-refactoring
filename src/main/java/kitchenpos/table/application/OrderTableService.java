package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.ui.request.OrderTableEmptyRequest;
import kitchenpos.table.ui.request.OrderTableGuestsRequest;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.table.ui.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toEntity());

        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
        final OrderTable orderTable = findById(orderTableId);

        Orders orders = new Orders(orderRepository.findAllByOrderTable(orderTable));
        orders.validateCompleted();
        orderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableGuestsRequest request) {
        OrderTable orderTable = findById(orderTableId);
        NumberOfGuests numberOfGuests = new NumberOfGuests(request.getNumberOfGuests());
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(
                String.format("%s ID에 해당하는 OrderTable이 존재하지 않습니다.", orderTableId)
            ));
    }
}
