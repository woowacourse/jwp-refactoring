package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.NumberOfGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderTableEmptyRequest;
import kitchenpos.ui.request.OrderTableGuestsRequest;
import kitchenpos.ui.request.OrderTableRequest;
import kitchenpos.ui.response.OrderTableResponse;
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
