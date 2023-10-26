package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.dto.ordertable.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.ordertable.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.ordertable.request.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Long create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = request.toEntity();
        return orderTableRepository.save(orderTable).id();
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] OrderTable이 존재하지 않습니다. id : " + orderTableId));

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.forEach(Order::validateStatus);

        savedOrderTable.changeEmpty(request.isEmpty());
    }

    @Transactional
    public void changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.numberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] OrderTable이 존재하지 않습니다. id : " + orderTableId));
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
    }
}
