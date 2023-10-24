package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }

        if (jpaOrderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return jpaOrderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = jpaOrderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return jpaOrderTableRepository.save(savedOrderTable);
    }
}
