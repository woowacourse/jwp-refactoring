package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.application.dto.OrderTableChangeGuestRequest;
import kitchenpos.ordertable.application.dto.OrderTableChangeStatusRequest;
import kitchenpos.ordertable.application.dto.OrderTableRequest;
import kitchenpos.ordertable.application.dto.OrderTableResponse;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
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
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeStatusRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        savedOrderTable.validateOrderTableHasTableGroupId();

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 주문 테이블은 상태를 변경할 수 없습니다.");
        }
        savedOrderTable.changeEmptyStatus(request.isEmpty());
        return OrderTableResponse.from(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableChangeGuestRequest orderTableChangeGuestRequest
    ) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        orderTable.changeNumberOfGuests(orderTableChangeGuestRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
