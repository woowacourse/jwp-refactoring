package kitchenpos.order.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.service.OrderTableService;
import kitchenpos.ordertable.service.dto.OrderTableRequest;
import kitchenpos.ordertable.service.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderTableServiceImpl implements OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableServiceImpl(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTable) {
        return OrderTableResponse.of(orderTableRepository.save(orderTable.toEntity()));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoSuchElementException::new);
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.forEach(Order::validateCompleted);

        savedOrderTable.changeEmpty(empty);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(NoSuchElementException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
