package kitchenpos.application;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = convertToOrderTable(request);

        orderTable.setId(null);
        orderTable.setTableGroupId(null);

        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return convertToOrderTableResponse(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return convertToOrderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = convertToOrderTable(request);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);
        return convertToOrderTableResponse(changedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable orderTable = convertToOrderTable(request);

        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        final OrderTable changedOrderTable = orderTableDao.save(savedOrderTable);
        return convertToOrderTableResponse(changedOrderTable);
    }

    private OrderTable convertToOrderTable(final OrderTableRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(request.isEmpty());
        orderTable.setNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }

    private OrderTableResponse convertToOrderTableResponse(final OrderTable orderTable) {
        return new OrderTableResponse(
            orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()
        );
    }

    private List<OrderTableResponse> convertToOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
