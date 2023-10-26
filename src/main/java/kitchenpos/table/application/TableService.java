package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.mapper.OrderTableMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableCreateRequest;
import kitchenpos.table.dto.TableUpdateEmptyRequest;
import kitchenpos.table.dto.TableUpdateNumberOfGuestsRequest;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final TableCreateRequest request) {
        final OrderTable orderTable = OrderTableMapper.toOrderTable(request);
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableMapper.toOrderTableResponse(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> readAll() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return OrderTableMapper.toOrderTableResponses(orderTables);
    }

    public OrderTableResponse changeEmpty(
            final Long orderTableId,
            final TableUpdateEmptyRequest request
    ) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        validateOrderStatus(orderTable);
        orderTable.updateEmpty(request.isEmpty());

        return OrderTableMapper.toOrderTableResponse(orderTable);
    }

    private void validateOrderStatus(final OrderTable orderTable) {
        final List<Long> orderTableIds = List.of(orderTable.getId());
        final List<OrderStatus> statuses = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, statuses)) {
            throw new IllegalArgumentException("상태가 COOKING, MEAL인 주문이 존재합니다.");
        }
    }

    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final TableUpdateNumberOfGuestsRequest request
    ) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());

        return OrderTableMapper.toOrderTableResponse(orderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문 테이블 입니다."));
    }
}
