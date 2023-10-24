package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.ChangeEmptyRequest;
import kitchenpos.dto.ChangeNumberOfGuestRequest;
import kitchenpos.dto.OrderTableRequest;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableRequest request) {
        final OrderTable orderTable = request.toOrderTable();
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeEmptyRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);

        final List<Order> orders = orderRepository.findByOrderByOrderTableId(orderTableId);
        final boolean containsNotCompletionOrder = orders.stream()
                .anyMatch(Order::isNotCompletionStatus);
        if (containsNotCompletionOrder) {
            throw new IllegalArgumentException("이미 주문이 진행 중이에요");
        }

        savedOrderTable.updateEmptyStatus(request.isEmpty());
        return savedOrderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("없는 테이블이에요"));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = getOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuest(request.getNumberOfGuest());

        return savedOrderTable;
    }
}
