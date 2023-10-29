package kitchenpos.table.application;

import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableUpdateRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.application.mapper.OrderTableMapper;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.getEmtpy());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableMapper.mapToOrderTableResponseBy(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableMapper::mapToOrderTableResponseBy)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        checkOrderStatusInTableGroup(orderTable);
        orderTable.changeEmpty(request.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableMapper.mapToOrderTableResponseBy(savedOrderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void checkOrderStatusInTableGroup(final OrderTable orderTable) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                List.of(orderTable), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableMapper.mapToOrderTableResponseBy(savedOrderTable);
    }
}
