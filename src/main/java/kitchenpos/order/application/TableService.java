package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderTableCreateRequest;
import kitchenpos.order.dto.OrderTableIsEmptyUpdateRequest;
import kitchenpos.order.dto.OrderTableNumberOfGuestsUpdateRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.vo.TableOrders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toOrderTableWithoutGroup());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeIsEmpty(Long orderTableId, OrderTableIsEmptyUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());

        orderTable.changeIsEmpty(request.isEmpty(), new TableOrders(orders));
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableNumberOfGuestsUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
