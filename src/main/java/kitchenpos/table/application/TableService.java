package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableChangNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(null, request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableBy(orderTableId);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 테이블은 존재하면서 결제완료 상태가 아니어야 합니다.");
        }

        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableChangNumberOfGuestRequest request) {
        final OrderTable savedOrderTable = findOrderTableBy(orderTableId);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableBy(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
