package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableUpdateRequest;
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

    public OrderTable create(final OrderTableCreateRequest request) {
        return orderTableRepository.save(new OrderTable(null,
                null,
                request.getNumberOfGuests(),
                request.isEmpty()));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        checkOrderIsInProgress(orderTableId);
        orderTable.updateEmpty(request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    private void checkOrderIsInProgress(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.collectInProgress())) {
            throw new IllegalArgumentException();
        }
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTableUpdateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTable.updateNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }
}
