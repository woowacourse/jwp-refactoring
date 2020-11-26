package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable savedOrderTable
            = orderTableRepository.save(orderTableCreateRequest.toEntity(null));

        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();

        return OrderTableResponse.toResponseList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
        final OrderTableChangeRequest orderTableChangeRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        validateStatus(orderTableId);

        savedOrderTable.clearOrderTable(savedOrderTable.getId(),
            savedOrderTable.getTableGroup(), savedOrderTable.getNumberOfGuests(),
            orderTableChangeRequest.isEmpty());
        OrderTable changedSavedOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changedSavedOrderTable);
    }

    private void validateStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableChangeRequest orderTableChangeRequest) {
        final int numberOfGuests = orderTableChangeRequest.getNumberOfGuests();

        orderTableChangeRequest.toEntity(null);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));

        savedOrderTable.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable.getTableGroup(),
            numberOfGuests, savedOrderTable.isEmpty());
        OrderTable changedSavedOrderTable = orderTableRepository.save(savedOrderTable);

        return OrderTableResponse.of(changedSavedOrderTable);
    }
}
