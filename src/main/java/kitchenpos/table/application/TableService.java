package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.dto.request.OrderTableCreateCommand;
import kitchenpos.table.application.dto.request.OrderTableEmptyCommand;
import kitchenpos.table.application.dto.request.OrderTableGuestCommand;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateCommand orderTableCreateCommand) {
        OrderTable orderTable = orderTableCreateCommand.toEntity();
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .toList();
    }

    public OrderTableResponse changeEmpty(final Long orderTableId,
                                          final OrderTableEmptyCommand orderTableEmptyCommand) {
        final OrderTable foundOrderTable = getOrderTable(orderTableId);
        validateOrderStatus(orderTableId);
        foundOrderTable.changeEmpty(orderTableEmptyCommand.empty());

        return OrderTableResponse.from(foundOrderTable);
    }

    private void validateOrderStatus(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 조리 / 식사 중입니다.");
        }
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableGuestCommand orderTableGuestCommand) {
        final OrderTable foundOrderTable = getOrderTable(orderTableId);
        foundOrderTable.changeNumberOfGuests(orderTableGuestCommand.numberOfGuests());
        return OrderTableResponse.from(foundOrderTable);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
    }
}
