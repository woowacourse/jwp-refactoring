package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
    public OrderTable create(final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(),
                orderTableRequest.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = getOrderTable(orderTableId);

        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹화된 테이블입니다.");
        }

        if (orderRepository.existsByOrderTableAndOrderStatusIn(orderTable,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 요리중이거나 식사중이여서 변경할 수 없습니다.");
        }

        orderTable.changeEmpty(orderTableRequest.getEmpty());
        return orderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수를 음수로 변경할 수 없습니다.");
        }

        final OrderTable orderTable = getOrderTable(orderTableId);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블입니다.");
        }

        orderTable.changeNumberOfGuests(numberOfGuests);
        return orderTable;
    }
}
