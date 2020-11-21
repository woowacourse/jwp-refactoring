package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableChangeRequest;
import kitchenpos.dto.OrderTableCreateRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
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
            .orElseThrow(IllegalArgumentException::new);

        validateTableGroup(savedOrderTable);
        validateStatus(orderTableId);

        OrderTable changedOrderTable = new OrderTable(savedOrderTable.getId(),
            savedOrderTable.getTableGroup(), savedOrderTable.getNumberOfGuests(),
            orderTableChangeRequest.isEmpty());
        OrderTable changedSavedOrderTable = orderTableRepository.save(changedOrderTable);

        return OrderTableResponse.of(changedSavedOrderTable);
    }

    private void validateTableGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final OrderTableChangeRequest orderTableChangeRequest) {
        final int numberOfGuests = orderTableChangeRequest.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);
        validateOrderTableEmpty(savedOrderTable);

        OrderTable changedOrderTable = new OrderTable(savedOrderTable.getId(),
            savedOrderTable.getTableGroup(), numberOfGuests,
            savedOrderTable.isEmpty());
        OrderTable changedSavedOrderTable = orderTableRepository.save(changedOrderTable);

        return OrderTableResponse.of(changedSavedOrderTable);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableEmpty(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
