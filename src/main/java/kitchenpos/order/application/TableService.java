package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
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

        return new OrderTableResponse(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty()
        );
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateGroup();

        Orders orders = new Orders(orderRepository.findAllByOrderTableId(savedOrderTable.getId()));
        orders.validateChangeEmpty();

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return new OrderTableResponse(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty()
        );
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateEmpty();

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return new OrderTableResponse(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty()
        );
    }
}
