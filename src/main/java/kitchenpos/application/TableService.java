package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.request.OrderTableRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final JpaOrderRepository jpaOrderRepository;
    private final JpaOrderTableRepository jpaOrderTableRepository;

    public TableService(
            final JpaOrderRepository jpaOrderRepository,
            final JpaOrderTableRepository jpaOrderTableRepository
    ) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.jpaOrderTableRepository = jpaOrderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = jpaOrderTableRepository.save(orderTable);
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = jpaOrderTableRepository.findAll();

        return orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (jpaOrderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(orderTableRequest.isEmpty());

        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.setNumberOfGuests(request.getNumberOfGuests());

        return new OrderTableResponse(savedOrderTable);
    }
}
