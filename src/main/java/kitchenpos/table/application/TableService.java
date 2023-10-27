package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.table.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        validateComplete(orders);
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    private void validateComplete(final List<Order> orders) {
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("조리, 식사 상태일 때는 빈 테이블로 변경할 수 없습니다.");
        }
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeNumberOfGuestsRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }
}
