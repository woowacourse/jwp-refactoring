package kitchenpos.application;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderTableRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao, final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.save(createOrderTable(request));
        return new OrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::new)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = createOrderTable(request);
        final OrderTable changedOrderTable = changeEmptyOfTable(orderTableId, orderTable);
        return new OrderTableResponse(changedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = createOrderTable(request);
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());

        final OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);
        return new OrderTableResponse(changedOrderTable);
    }

    private OrderTable createOrderTable(final OrderTableRequest request) {
        return new OrderTable(
            request.getNumberOfGuests(),
            request.isEmpty()
        );
    }

    private OrderTable changeEmptyOfTable(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);

        validateOrderTableIsNotExists(savedOrderTable);
        validateOrderStatusIsCompletion(orderTableId);

        savedOrderTable.changeEmpty(orderTable.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 테이블 입니다. [%s]", orderTableId)));
    }

    private void validateOrderTableIsNotExists(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException(String.format("테이블 그룹이 존재합니다. [%s]", savedOrderTable.getTableGroupId()));
        }
    }

    private void validateOrderStatusIsCompletion(final Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 완료되지 않았습니다.");
        }
    }
}
