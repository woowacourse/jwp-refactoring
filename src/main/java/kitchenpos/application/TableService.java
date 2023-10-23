package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.table.OrderTableResponse;
import kitchenpos.dto.table.TableCreateRequest;
import kitchenpos.dto.table.TableUpdateEmptyRequest;
import kitchenpos.dto.table.TableUpdateNumberOfGuestsRequest;
import kitchenpos.mapper.OrderTableMapper;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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

    public OrderTableResponse create(
            final TableCreateRequest request
    ) {
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

    private void validateOrderStatus(
            final OrderTable savedOrderTable
    ) {
        if (orderRepository.existsByOrderTableAndOrderStatusIn(
                savedOrderTable, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
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

    private OrderTable findOrderTableById(
            final Long orderTableId
    ) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 주문 테이블 입니다."));
    }
}
