package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest dto) {
        final OrderTable orderTable = new OrderTable(dto.getNumberOfGuests(), dto.isEmpty());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);
        return new OrderTableResponse(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroupId(),
                savedOrderTable.getNumberOfGuests(),
                savedOrderTable.isEmpty()
        );
    }

    public List<OrderTableResponse> list() {
        final List<OrderTable> orderTables = orderTableDao.findAll();
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(
                        orderTable.getId(),
                        orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(),
                        orderTable.isEmpty()
                )).collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableChangeEmptyRequest dto) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.validateTableGroupIdNotNull();
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(dto.isEmpty());
        final OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableChangeNumberOfGuestRequest dto) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(dto.getNumberOfGuests());
        final OrderTable orderTable = orderTableDao.save(savedOrderTable);
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
    }
}
