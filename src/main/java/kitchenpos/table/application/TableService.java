package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.ui.dto.OrderTableResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.ui.dto.OrderTableCreateRequest;
import kitchenpos.table.ui.dto.TableChangeEmptyRequest;
import kitchenpos.table.ui.dto.TableChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableDao.save(request.toEntity());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableChangeEmptyRequest request) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        validateTableGroupIsNull(orderTable);
        validateOrderStatus(orderTableId);

        OrderTable updateOrderTable = new OrderTable(orderTableId, orderTable.getNumberOfGuests(), request.isEmpty());
        OrderTable savedOrderTable = orderTableDao.save(updateOrderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    private void validateTableGroupIsNull(final OrderTable orderTable) {
        if (orderTable.isTableGroupIdNotNull()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderStatus(final Long orderTableId) {
        List<String> conditions = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        boolean existsOrderInConditions = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, conditions);

        if (existsOrderInConditions) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final TableChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        //
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        OrderTable updatedOrderTable = orderTableDao.save(savedOrderTable);
        return OrderTableResponse.of(updatedOrderTable);
    }
}
