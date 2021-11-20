package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.dto.request.OrderTableRequest;
import kitchenpos.order.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = new OrderTable(
                null,
                orderTableRequest.getNumberOfGuests(),
                orderTableRequest.isEmpty()
        );
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.toList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateGroup();

        Orders orders = new Orders(orderRepository.findAllByOrderTableId(savedOrderTable.getId()));
        orders.validateChangeEmpty();

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmpty();

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(savedOrderTable);
    }
}
